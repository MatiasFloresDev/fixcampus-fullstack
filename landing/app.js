'use strict';
(() => {
  let language = localStorage.getItem('fixcampus-language') === 'en_US' ? 'en' : 'es';
  const toggle = document.querySelector('#language');
  function translate() {
    document.documentElement.lang = language === 'es' ? 'es-419' : 'en-US';
    document.querySelectorAll('[data-es][data-en]').forEach(element => { element.textContent = element.dataset[language]; });
    document.title = language === 'es' ? 'FixCampus · Un mejor campus empieza contigo' : 'FixCampus · A better campus starts with you';
    toggle.textContent = language === 'es' ? 'ES / EN' : 'EN / ES';
    toggle.setAttribute('aria-label', language === 'es' ? 'Cambiar idioma a inglés' : 'Switch language to Spanish');
    document.querySelector('nav').setAttribute('aria-label', language === 'es' ? 'Navegación principal' : 'Main navigation');
    document.querySelector('.illustration').setAttribute('aria-label', language === 'es' ? 'Ilustración de un campus con edificios, árboles y un camino accesible' : 'Campus illustration with buildings, trees and an accessible path');
  }
  toggle.addEventListener('click', () => { language = language === 'es' ? 'en' : 'es'; localStorage.setItem('fixcampus-language', language === 'es' ? 'es_419' : 'en_US'); translate(); });
  document.querySelectorAll('[data-app-link]').forEach(link => { link.href = window.FIXCAMPUS_CONFIG?.appUrl || 'http://localhost:4200'; });
  document.querySelectorAll('[data-dialog]').forEach(button => button.addEventListener('click', () => document.getElementById(button.dataset.dialog).showModal()));
  translate();
})();
