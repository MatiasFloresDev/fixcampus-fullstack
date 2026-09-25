import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Api } from './api.service';
import { I18n } from './i18n.service';
import { MATERIAL } from './material';

@Component({
  standalone: true,
  imports: [FormsModule, RouterLink, ...MATERIAL],
  template: `
<section class="login-layout">
  <div class="login-story">
    <h1>{{i.t('Un mejor campus empieza contigo.','A better campus starts with you.')}}</h1>
    <p>{{i.t('Crea tu cuenta para registrar incidencias y consultar su atención.','Create an account to report issues and follow their progress.')}}</p>
    <div class="campus-art" aria-hidden="true"><span class="art-sun"></span><div class="building a"></div><div class="building b"></div><div class="building c"></div><span class="art-tree"></span><span class="art-path"></span></div>
    <span class="story-caption">{{i.t('Una cuenta, un canal claro para cuidar el campus.','One account, one clear channel to care for the campus.')}}</span>
  </div>
  <div class="panel login-card">
    <div class="brand"><span class="brand-mark">F<span>+</span></span> FixCampus</div>
    <h2>{{i.t('Crear una cuenta','Create an account')}}</h2>
    <p class="muted">{{i.t('Regístrate como estudiante o miembro de la comunidad.','Register as a student or community member.')}}</p>
    <form #form="ngForm" (ngSubmit)="register()">
      <mat-form-field appearance="outline"><mat-label>{{i.t('Nombre completo','Full name')}}</mat-label><input matInput type="text" name="name" [(ngModel)]="name" required maxlength="120" autocomplete="name"><mat-error>{{i.t('Escribe tu nombre.','Enter your name.')}}</mat-error></mat-form-field>
      <mat-form-field appearance="outline"><mat-label>{{i.t('Correo electrónico','Email address')}}</mat-label><input matInput type="email" name="email" [(ngModel)]="email" required email maxlength="180" autocomplete="email"><mat-error>{{i.t('Ingresa un correo válido.','Enter a valid email address.')}}</mat-error></mat-form-field>
      <mat-form-field appearance="outline"><mat-label>{{i.t('Contraseña','Password')}}</mat-label><input matInput type="password" name="password" [(ngModel)]="password" required minlength="12" maxlength="72" autocomplete="new-password"><mat-hint>{{i.t('Mínimo 12 caracteres.','At least 12 characters.')}}</mat-hint><mat-error>{{i.t('Usa al menos 12 caracteres.','Use at least 12 characters.')}}</mat-error></mat-form-field>
      <mat-form-field appearance="outline"><mat-label>{{i.t('Repetir contraseña','Confirm password')}}</mat-label><input matInput type="password" name="confirmPassword" [(ngModel)]="confirmPassword" required autocomplete="new-password"><mat-error>{{i.t('Repite la contraseña.','Repeat your password.')}}</mat-error></mat-form-field>
      @if(password && confirmPassword && password !== confirmPassword){<p role="alert" class="alert error">{{i.t('Las contraseñas no coinciden.','Passwords do not match.')}}</p>}
      @if(error){<p role="alert" class="alert error">{{error}}</p>}
      @if(success){<p role="status" class="alert success">{{success}}</p>}
      @if(busy){<mat-progress-bar mode="indeterminate" [attr.aria-label]="i.t('Creando cuenta','Creating account')"/>}
      <button mat-flat-button class="full-width" type="submit" [disabled]="form.invalid || password !== confirmPassword || busy">{{i.t('Crear cuenta','Create account')}} <span aria-hidden="true">→</span></button>
    </form>
    <p class="login-help">{{i.t('¿Ya tienes una cuenta?','Already have an account?')}} <a routerLink="/login">{{i.t('Iniciar sesión','Sign in')}}</a></p>
  </div>
</section>`
})
export class RegisterComponent {
  api=inject(Api); i=inject(I18n); router=inject(Router);
  name=''; email=''; password=''; confirmPassword=''; busy=false; error=''; success='';
  async register(){
    this.busy=true; this.error=''; this.success='';
    try { await this.api.register(this.name,this.email,this.password); this.success=this.i.t('Cuenta creada. Ya puedes iniciar sesión.','Account created. You can now sign in.'); setTimeout(()=>this.router.navigate(['/login']),900); }
    catch(e){ this.error=this.api.error(e); }
    finally { this.busy=false; }
  }
}
