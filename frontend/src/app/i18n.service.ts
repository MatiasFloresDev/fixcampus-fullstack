import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class I18n {
  readonly locale = signal<'es_419'|'en_US'>(localStorage.getItem('fixcampus-language') === 'en_US' ? 'en_US' : 'es_419');
  t(es: string, en: string): string { return this.locale() === 'es_419' ? es : en; }
  toggle(): void { this.locale.set(this.locale() === 'es_419' ? 'en_US' : 'es_419'); localStorage.setItem('fixcampus-language', this.locale()); document.documentElement.lang = this.locale().replace('_', '-'); }
  label(value: string): string {
    const labels: Record<string,[string,string]> = { NEW:['Nuevo','New'],ASSIGNED:['Asignado','Assigned'],IN_PROGRESS:['En atención','In progress'],RESOLVED:['Resuelto','Resolved'],CLOSED:['Cerrado','Closed'],LOW:['Baja','Low'],MEDIUM:['Media','Medium'],HIGH:['Alta','High'],CRITICAL:['Crítica','Critical'],REPORTER:['Reportante','Reporter'],TECHNICIAN:['Técnico','Technician'],ADMIN:['Administrador','Administrator'] };
    return labels[value] ? this.t(...labels[value]) : value;
  }
  date(value: string): string { return new Intl.DateTimeFormat(this.locale().replace('_','-'),{dateStyle:'medium',timeStyle:'short'}).format(new Date(value)); }
}
