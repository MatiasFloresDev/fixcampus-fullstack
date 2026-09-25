import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Api } from './api.service';
import { I18n } from './i18n.service';
import { MATERIAL } from './material';
@Component({standalone:true,imports:[FormsModule,RouterLink,...MATERIAL],template:`
<section class="login-layout">
  <div class="login-story"><h1>{{i.t('Un mejor campus empieza contigo.','A better campus starts with you.')}}</h1><p>{{i.t('Reporta un problema, sigue su atención y contribuye a espacios más seguros para nuestra comunidad.','Report an issue, follow its progress and contribute to safer spaces for our community.')}}</p><img class="campus-photo" src="/images/biblioteca.jpg" width="1200" height="800" alt="Estanterías y pasillo de una biblioteca" fetchpriority="high"><span class="story-caption">{{i.t('Conectamos a quienes reportan con quienes resuelven.','Connecting people who report with people who resolve.')}}</span></div>
  <div class="panel login-card"><div class="brand"><span class="brand-mark">F<span>+</span></span> FixCampus</div><h2>{{i.t('Te damos la bienvenida','Welcome back')}}</h2><p class="muted">{{i.t('Ingresa con tu cuenta de la plataforma.','Sign in with your platform account.')}}</p>
  <form #form="ngForm" (ngSubmit)="login()">
    <mat-form-field appearance="outline"><mat-label>{{i.t('Correo electrónico','Email address')}}</mat-label><input matInput type="email" name="email" [(ngModel)]="email" required email autocomplete="username" #emailField="ngModel"><mat-error>{{i.t('Ingresa un correo válido.','Enter a valid email address.')}}</mat-error></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>{{i.t('Contraseña','Password')}}</mat-label><input matInput type="password" name="password" [(ngModel)]="password" required autocomplete="current-password"><mat-error>{{i.t('La contraseña es obligatoria.','Password is required.')}}</mat-error></mat-form-field>
    @if(error){<p role="alert" class="alert error">{{error}}</p>}
    @if(busy){<mat-progress-bar mode="indeterminate" [attr.aria-label]="i.t('Iniciando sesión','Signing in')"/>}
    <button mat-flat-button class="full-width" type="submit" [disabled]="form.invalid || busy">{{i.t('Ingresar a FixCampus','Sign in to FixCampus')}} <span aria-hidden="true">→</span></button>
  </form><p class="login-help">{{i.t('¿Todavía no tienes una cuenta?','Do not have an account yet?')}} <a routerLink="/register">{{i.t('Crear cuenta','Create account')}}</a></p></div>
</section>`})
export class LoginComponent {api=inject(Api);i=inject(I18n);router=inject(Router);email='';password='';busy=false;error='';async login(){this.busy=true;this.error='';try{await this.api.login(this.email,this.password);await this.router.navigate(['/dashboard']);}catch(e){this.error=this.api.error(e);}finally{this.busy=false;}}}

