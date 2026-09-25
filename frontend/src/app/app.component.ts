import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Api } from './api.service';
import { I18n } from './i18n.service';
import { MATERIAL } from './material';

@Component({selector:'app-root',standalone:true,imports:[RouterOutlet,RouterLink,RouterLinkActive,...MATERIAL],template:`
  <a class="skip-link" href="#main">{{i.t('Saltar al contenido','Skip to content')}}</a>
  <div class="shell" [class.public-shell]="!api.user()">
    @if(api.user(); as user) {
      <aside class="sidebar">
        <a routerLink="/dashboard" class="brand"><span class="brand-mark">F<span>+</span></span> FixCampus</a>
        <p class="sidebar-label">{{i.t('CUIDAMOS EL CAMPUS','CAMPUS CARE')}}</p>
        <nav [attr.aria-label]="i.t('Navegación principal','Main navigation')">
          <a routerLink="/dashboard" routerLinkActive="active"><span aria-hidden="true">◫</span>{{i.t('Vista general','Overview')}}</a>
          <a routerLink="/reports" routerLinkActive="active"><span aria-hidden="true">▤</span>{{i.t('Incidencias','Reports')}}</a>
          @if(user.role === 'ADMIN') {<a routerLink="/admin" routerLinkActive="active"><span aria-hidden="true">⚙</span>{{i.t('Administración','Administration')}}</a>}
        </nav>
        <div class="sidebar-bottom"><span class="ods-number">11</span><div><strong>{{i.t('Un campus para todos','A campus for everyone')}}</strong><p>{{i.t('Espacios seguros e inclusivos.','Safe, inclusive spaces.')}}</p></div></div>
      </aside>
    }
    <div class="workspace">
      <header class="topbar">
        <span class="context">{{i.t('Gestión de incidencias del campus','Campus incident management')}}</span>
        <div class="top-actions"><button mat-button (click)="i.toggle()" [attr.aria-label]="i.t('Cambiar idioma a inglés','Switch language to Spanish')">{{i.locale() === 'es_419' ? 'ES / EN' : 'EN / ES'}}</button>
        @if(api.user(); as user) {<span class="user-badge"><span class="avatar">{{user.name.charAt(0)}}</span><span>{{user.name}}<small>{{i.label(user.role)}}</small></span></span><button mat-button (click)="logout()">{{i.t('Salir','Log out')}}</button>}</div>
      </header>
      @if(error) {<p role="alert" class="alert error">{{error}}</p>}
      <main id="main" tabindex="-1"><router-outlet /></main>
      <footer class="app-footer">FixCampus · {{i.t('Proyecto académico UPC','UPC academic project')}} <span>{{i.t('Cada reporte es una oportunidad de mejorar.','Every report is an opportunity to improve.')}}</span></footer>
    </div>
  </div>`})
export class AppComponent {
  api=inject(Api); i=inject(I18n); router=inject(Router); error='';
  constructor(){document.documentElement.lang=this.i.locale().replace('_','-');}
  async logout(){try{await this.api.logout();await this.router.navigate(['/login']);}catch(e){this.error=this.api.error(e);}}
}
