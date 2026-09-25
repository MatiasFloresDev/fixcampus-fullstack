import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { User } from './models';

@Injectable({ providedIn: 'root' })
export class Api {
  private http = inject(HttpClient);
  readonly user = signal<User|null>(null);
  private csrf: {token:string;headerName:string}|null = null;
  get<T>(path:string):Promise<T> { return firstValueFrom(this.http.get<T>('/api'+path,{withCredentials:true})); }
  async write<T>(method:'POST'|'PUT'|'DELETE',path:string,body:unknown = {}):Promise<T> {
    if (!this.csrf) this.csrf = await this.get('/auth/csrf');
    return firstValueFrom(this.http.request<T>(method,'/api'+path,{body,withCredentials:true,headers:new HttpHeaders({[this.csrf!.headerName]:this.csrf!.token})}));
  }
  async restore():Promise<void> { try { this.user.set(await this.get<User>('/auth/me')); } catch { this.user.set(null); } }
  async login(email:string,password:string):Promise<void> { const user = await this.write<User>('POST','/auth/login',{email,password}); this.csrf = null; this.user.set(user); }
  async logout():Promise<void> { await this.write('POST','/auth/logout'); this.csrf = null; this.user.set(null); }
  error(error:unknown):string { if(error instanceof HttpErrorResponse) return error.status === 0 ? 'No se pudo conectar al servidor / Cannot connect to server.' : error.error?.message || `HTTP ${error.status}`; return 'Ocurrió un error inesperado / An unexpected error occurred.'; }
}
