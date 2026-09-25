import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { DatePipe, NgOptimizedImage } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { finalize, timeout } from 'rxjs';
import { environment } from '../environments/environment';

interface Categoria {
  idCategoria: number;
  nombre: string;
}

interface Ubicacion {
  idUbicacion: number;
  campus: string;
  edificio: string;
  piso: number | null;
  zona: string | null;
  tipo: string;
}

interface Reporte {
  idReporte: number;
  usuarioReportanteId: number;
  tecnicoAsignadoId: number | null;
  categoriaId: number;
  titulo: string;
  categoriaNombre: string;
  ubicacionId: number;
  descripcion: string;
  detalleUbicacion: string | null;
  prioridad: string | null;
  estado: string;
  fechaCreacion: string;
}

interface Adjunto {
  idAdjunto: number;
  reporteId: number;
  nombreArchivo: string;
  urlArchivo: string;
  tipoArchivo: string;
  fechaSubida: string;
}

interface LoginResponse {
  token: string;
  correo: string;
  idUsuario: number;
}

type Pagina = 'inicio' | 'registrarse' | 'iniciar-sesion' | 'reportar';

interface Prioridad {
  valor: string;
  nombre: string;
}

@Component({
  selector: 'app-root',
  imports: [FormsModule, NgOptimizedImage, DatePipe],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  private readonly http = inject(HttpClient);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly apiUrl = environment.apiUrl;

  paginaActual: Pagina = this.obtenerPagina();
  reportes: Reporte[] = [];
  categorias: Categoria[] = [];
  ubicaciones: Ubicacion[] = [];
  prioridades: Prioridad[] = [
    { valor: 'MUY_BAJA', nombre: 'Muy baja' },
    { valor: 'BAJA', nombre: 'Baja' },
    { valor: 'MEDIA', nombre: 'Media' },
    { valor: 'ALTA', nombre: 'Alta' },
    { valor: 'MUY_ALTA', nombre: 'Muy alta' }
  ];
  isAuthenticated = false;
  usuarioActual = '';
  registerMessage = '';
  loginMessage = '';
  reportMessage = '';
  cargandoReportes = false;
  cargandoOpciones = false;
  enviandoReporte = false;
  enviandoCuenta = false;
  enviandoLogin = false;
  subiendoEvidencia = false;
  menuAbierto = false;
  evidenciaSeleccionada: File | null = null;
  evidenciaPreviewUrl = '';
  evidenciaMessage = '';
  mostrarDuplicado = false;
  reporteSimilar: Reporte | null = null;
  confirmarDuplicado = false;
  reporteEditandoId: number | null = null;

  authForm = { nombre: '', apellido: '', correo: '', password: '' };
  nuevoReporte = {
    titulo: '',
    categoriaId: 0,
    ubicacionId: 0,
    detalleUbicacion: '',
    descripcion: '',
    prioridad: 'MEDIA'
  };

  constructor() {
    const token = localStorage.getItem('fixcampus_token');
    const correo = localStorage.getItem('fixcampus_usuario');
    if (token && correo && !this.tokenVencido(token)) {
      this.usuarioActual = correo;
      this.isAuthenticated = true;
      if (this.paginaActual === 'reportar') {
        this.cargarDatos();
      }
    } else {
      this.limpiarSesion();
    }

    window.addEventListener('popstate', () => {
      this.paginaActual = this.obtenerPagina();
      this.menuAbierto = false;
    });
  }

  alternarMenu(): void {
    this.menuAbierto = !this.menuAbierto;
  }

  cerrarMenu(): void {
    this.menuAbierto = false;
  }

  private obtenerPagina(): Pagina {
    const ruta = window.location.pathname.replace(/\/$/, '');
    if (ruta === '/registrarse') return 'registrarse';
    if (ruta === '/iniciar-sesion') return 'iniciar-sesion';
    if (ruta === '/reportar') return 'reportar';
    return 'inicio';
  }

  private tokenVencido(token: string): boolean {
    try {
      const datos = JSON.parse(atob(token.split('.')[1])) as { exp?: number };
      return !datos.exp || datos.exp * 1000 <= Date.now();
    } catch {
      return true;
    }
  }

  private limpiarSesion(): void {
    localStorage.removeItem('fixcampus_token');
    localStorage.removeItem('fixcampus_usuario');
    localStorage.removeItem('fixcampus_id_usuario');
    this.usuarioActual = '';
    this.isAuthenticated = false;
    this.reportes = [];
  }

  private cabeceras(): { headers: HttpHeaders } {
    return { headers: new HttpHeaders({ Authorization: `Bearer ${localStorage.getItem('fixcampus_token')}` }) };
  }

  private errorApi(error: { status?: number; error?: { message?: string } }, mensaje: string): string {
    if (error.status === 401) {
      this.limpiarSesion();
      return 'Tu sesión terminó. Inicia sesión nuevamente.';
    }
    if (error.status === 0) return 'No se pudo conectar con el servidor. Intenta de nuevo en unos minutos.';
    return error.error?.message ?? mensaje;
  }

  registrarCuenta(): void {
    const nombre = this.authForm.nombre.trim();
    const apellido = this.authForm.apellido.trim();
    const correo = this.authForm.correo.trim().toLowerCase();
    const password = this.authForm.password;
    if (!nombre || !apellido || !correo || password.length < 6) {
      this.registerMessage = 'Completa tus datos y usa una contraseña de al menos 6 caracteres.';
      return;
    }
    this.enviandoCuenta = true;
    this.registerMessage = '';
    this.http.post(`${this.apiUrl}/registro`, { nombre, apellido, correo, password }).pipe(
      timeout(10000),
      finalize(() => {
        this.enviandoCuenta = false;
        this.cdr.detectChanges();
      })
    ).subscribe({
      next: () => {
        this.registerMessage = 'Cuenta creada. Ya puedes iniciar sesión.';
        this.authForm = { nombre: '', apellido: '', correo, password: '' };
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.registerMessage = this.errorApi(error, 'No se pudo crear la cuenta.');
        this.cdr.detectChanges();
      }
    });
  }

  iniciarSesion(): void {
    this.enviandoLogin = true;
    this.loginMessage = '';
    this.http.post<LoginResponse>(`${this.apiUrl}/login`, {
      correo: this.authForm.correo.trim().toLowerCase(),
      password: this.authForm.password
    }).subscribe({
      next: (respuesta) => {
        this.enviandoLogin = false;
        localStorage.setItem('fixcampus_token', respuesta.token);
        localStorage.setItem('fixcampus_usuario', respuesta.correo);
        localStorage.setItem('fixcampus_id_usuario', String(respuesta.idUsuario));
        this.usuarioActual = respuesta.correo;
        this.isAuthenticated = true;
        this.authForm.password = '';
        this.cdr.detectChanges();
        window.location.href = '/reportar';
      },
      error: () => {
        this.enviandoLogin = false;
        this.loginMessage = 'Correo o contraseña incorrectos. Revisa los datos e intenta de nuevo.';
        this.cdr.detectChanges();
      }
    });
  }

  cerrarSesion(): void {
    this.limpiarSesion();
    this.reportMessage = '';
    if (this.paginaActual === 'reportar') window.location.href = '/';
  }

  editarReporte(reporte: Reporte): void {
    this.reporteEditandoId = reporte.idReporte;
    this.nuevoReporte = {
      titulo: reporte.titulo,
      categoriaId: reporte.categoriaId,
      ubicacionId: reporte.ubicacionId,
      detalleUbicacion: reporte.detalleUbicacion ?? '',
      descripcion: reporte.descripcion,
      prioridad: reporte.prioridad ?? 'MEDIA'
    };
    this.reportMessage = '';
    this.mostrarDuplicado = false;
    this.quitarEvidencia();
    window.scrollTo({ top: 0, behavior: 'smooth' });
    this.cdr.detectChanges();
  }

  cancelarEdicionReporte(): void {
    this.reporteEditandoId = null;
    this.nuevoReporte = { titulo: '', categoriaId: 0, ubicacionId: 0, detalleUbicacion: '', descripcion: '', prioridad: 'MEDIA' };
    this.reportMessage = '';
    this.quitarEvidencia();
    this.cdr.detectChanges();
  }

  nombrePrioridad(valor: string | null): string {
    return this.prioridades.find(prioridad => prioridad.valor === valor)?.nombre ?? valor ?? 'Sin prioridad';
  }

  seleccionarEvidencia(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    this.evidenciaMessage = '';
    this.evidenciaPreviewUrl = '';
    this.evidenciaSeleccionada = null;
    if (!file) return;

    const tiposPermitidos = ['image/jpeg', 'image/png', 'application/pdf'];
    if (!tiposPermitidos.includes(file.type)) {
      this.evidenciaMessage = 'Solo se permiten imágenes JPG, PNG o archivos PDF.';
      input.value = '';
      return;
    }
    if (file.size > 5 * 1024 * 1024) {
      this.evidenciaMessage = 'La evidencia no puede superar los 5 MB.';
      input.value = '';
      return;
    }

    this.evidenciaSeleccionada = file;
    if (file.type.startsWith('image/')) {
      this.evidenciaPreviewUrl = URL.createObjectURL(file);
    }
    this.cdr.detectChanges();
  }

  quitarEvidencia(input?: HTMLInputElement): void {
    if (this.evidenciaPreviewUrl) URL.revokeObjectURL(this.evidenciaPreviewUrl);
    this.evidenciaPreviewUrl = '';
    this.evidenciaSeleccionada = null;
    this.evidenciaMessage = '';
    (input ?? document.getElementById('evidencia'))?.setAttribute('value', '');
    if (input) input.value = '';
    const fileInput = document.getElementById('evidencia') as HTMLInputElement | null;
    if (fileInput) fileInput.value = '';
  }

  confirmarEnvioDuplicado(): void {
    this.mostrarDuplicado = false;
    this.confirmarDuplicado = true;
    this.guardarReporte();
  }

  private encontrarReporteSimilar(): Reporte | null {
    const categoriaId = this.nuevoReporte.categoriaId;
    const ubicacionId = this.nuevoReporte.ubicacionId;
    const titulo = this.nuevoReporte.titulo.trim().toLowerCase();
    const palabras = titulo.split(/\s+/).filter(palabra => palabra.length >= 4);
    const limite = Date.now() - 72 * 60 * 60 * 1000;

    return this.reportes.find(reporte => {
      const fecha = Date.parse(reporte.fechaCreacion);
      if (!Number.isNaN(fecha) && fecha < limite) return false;
      if (reporte.categoriaId !== categoriaId || reporte.ubicacionId !== ubicacionId) return false;
      const tituloExistente = reporte.titulo.toLowerCase();
      return palabras.length === 0 || palabras.some(palabra => tituloExistente.includes(palabra));
    }) ?? null;
  }

  private finalizarEnvioReporte(mensaje: string): void {
    this.enviandoReporte = false;
    this.subiendoEvidencia = false;
    this.reportMessage = mensaje;
    this.reporteEditandoId = null;
    this.nuevoReporte = { titulo: '', categoriaId: 0, ubicacionId: 0, detalleUbicacion: '', descripcion: '', prioridad: 'MEDIA' };
    this.quitarEvidencia();
    this.cargarReportes();
    this.cdr.detectChanges();
  }

  private cargarDatos(): void {
    this.cargandoOpciones = true;
    this.http.get<Categoria[]>(`${this.apiUrl}/api/categories`, this.cabeceras()).subscribe({
      next: (categorias) => {
        this.categorias = categorias;
        const nombre = new URLSearchParams(window.location.search).get('categoria');
        const elegida = categorias.find(c => c.nombre.toLowerCase() === nombre?.toLowerCase());
        if (elegida) this.nuevoReporte.categoriaId = elegida.idCategoria;
        this.cargandoOpciones = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.cargandoOpciones = false;
        this.reportMessage = this.errorApi(error, 'No se pudieron cargar las categorías.');
        this.cdr.detectChanges();
      }
    });
    this.http.get<Ubicacion[]>(`${this.apiUrl}/api/locations`, this.cabeceras()).subscribe({
      next: (ubicaciones) => {
        this.ubicaciones = ubicaciones;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.reportMessage = this.errorApi(error, 'No se pudieron cargar las ubicaciones.');
        this.cdr.detectChanges();
      }
    });
    this.cargarReportes();
  }

  private cargarReportes(): void {
    this.cargandoReportes = true;
    this.http.get<Reporte[]>(`${this.apiUrl}/api/reports/mis-reportes`, this.cabeceras()).subscribe({
      next: (reportes) => {
        this.reportes = reportes.sort((a, b) => b.idReporte - a.idReporte);
        this.cargandoReportes = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.cargandoReportes = false;
        this.reportMessage = this.errorApi(error, 'No se pudieron cargar tus reportes.');
        this.cdr.detectChanges();
      }
    });
  }

  nombreUbicacion(id: number): string {
    const ubicacion = this.ubicaciones.find(u => u.idUbicacion === id);
    return ubicacion ? `${ubicacion.campus} · ${ubicacion.edificio}` : 'Ubicación registrada';
  }

  guardarReporte(): void {
    if (!this.isAuthenticated) {
      window.location.href = '/iniciar-sesion';
      return;
    }
    if (!this.nuevoReporte.titulo.trim() || !this.nuevoReporte.descripcion.trim()
      || !this.nuevoReporte.categoriaId || !this.nuevoReporte.ubicacionId) {
      this.reportMessage = 'Completa el título, la categoría, la ubicación y la descripción.';
      return;
    }
    if (!this.reporteEditandoId && !this.confirmarDuplicado) {
      this.reporteSimilar = this.encontrarReporteSimilar();
      if (this.reporteSimilar) {
        this.mostrarDuplicado = true;
        this.reportMessage = '';
        this.cdr.detectChanges();
        return;
      }
    }
    this.confirmarDuplicado = false;
    this.mostrarDuplicado = false;
    this.enviandoReporte = true;
    this.reportMessage = '';
    const datosReporte = {
      usuarioReportanteId: Number(localStorage.getItem('fixcampus_id_usuario')) || null,
      tecnicoAsignadoId: this.reporteEditandoId ? (this.reportes.find(reporte => reporte.idReporte === this.reporteEditandoId)?.tecnicoAsignadoId ?? null) : null,
      categoriaId: this.nuevoReporte.categoriaId,
      ubicacionId: this.nuevoReporte.ubicacionId,
      titulo: this.nuevoReporte.titulo.trim(),
      descripcion: this.nuevoReporte.descripcion.trim(),
      detalleUbicacion: this.nuevoReporte.detalleUbicacion.trim(),
      prioridad: this.nuevoReporte.prioridad,
      estado: this.reporteEditandoId ? (this.reportes.find(reporte => reporte.idReporte === this.reporteEditandoId)?.estado ?? 'ABIERTO') : 'ABIERTO'
    };
    const solicitud = this.reporteEditandoId
      ? this.http.put<Reporte>(`${this.apiUrl}/api/reports/${this.reporteEditandoId}`, datosReporte, this.cabeceras())
      : this.http.post<Reporte>(`${this.apiUrl}/api/reports`, datosReporte, this.cabeceras());
    solicitud.subscribe({
      next: (reporteCreado) => {
        if (!this.evidenciaSeleccionada) {
          this.finalizarEnvioReporte(this.reporteEditandoId ? 'Reporte actualizado correctamente.' : 'Reporte enviado correctamente. Ya aparece en Mis reportes.');
          return;
        }

        const formData = new FormData();
        formData.append('reporteId', String(reporteCreado.idReporte));
        formData.append('file', this.evidenciaSeleccionada);
        this.subiendoEvidencia = true;
        this.http.post<Adjunto>(`${this.apiUrl}/api/attachments/upload`, formData, this.cabeceras()).pipe(
          timeout(15000)
        ).subscribe({
          next: () => this.finalizarEnvioReporte(this.reporteEditandoId ? 'Reporte y evidencia actualizados correctamente.' : 'Reporte y evidencia enviados correctamente.'),
          error: () => this.finalizarEnvioReporte(this.reporteEditandoId ? 'El reporte se actualizó, pero no se pudo subir la evidencia.' : 'El reporte se creó, pero no se pudo subir la evidencia. Puedes intentarlo nuevamente desde el reporte.')
        });
      },
      error: (error) => {
        this.enviandoReporte = false;
        this.reportMessage = this.errorApi(error, 'No se pudo enviar el reporte.');
        this.cdr.detectChanges();
      }
    });
  }
}
