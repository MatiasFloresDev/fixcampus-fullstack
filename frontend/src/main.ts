import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter, Routes } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from './app/app.component';
import { Api } from './app/api.service';
import { LoginComponent } from './app/login.component';
import { DashboardComponent } from './app/dashboard.component';
import { ReportsComponent } from './app/reports.component';
import { ReportFormComponent } from './app/report-form.component';
import { ReportDetailComponent } from './app/report-detail.component';
import { AdminComponent } from './app/admin.component';

const authenticated = async () => { const api=inject(Api),router=inject(Router); if(!api.user()) await api.restore(); return api.user() ? true : router.createUrlTree(['/login']); };
const administrator = async () => { const api=inject(Api),router=inject(Router); if(!api.user()) await api.restore(); return api.user()?.role==='ADMIN' ? true : router.createUrlTree(['/dashboard']); };
const routes:Routes = [
  {path:'login',component:LoginComponent},
  {path:'dashboard',component:DashboardComponent,canActivate:[authenticated]},
  {path:'reports',component:ReportsComponent,canActivate:[authenticated]},
  {path:'reports/new',component:ReportFormComponent,canActivate:[authenticated]},
  {path:'reports/:id/edit',component:ReportFormComponent,canActivate:[authenticated]},
  {path:'reports/:id',component:ReportDetailComponent,canActivate:[authenticated]},
  {path:'admin',component:AdminComponent,canActivate:[administrator]},
  {path:'',pathMatch:'full',redirectTo:'dashboard'},
  {path:'**',redirectTo:'dashboard'}
];
bootstrapApplication(AppComponent,{providers:[provideHttpClient(),provideAnimationsAsync(),provideRouter(routes)]}).catch(console.error);
