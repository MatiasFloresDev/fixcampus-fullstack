export type Role = 'REPORTER' | 'TECHNICIAN' | 'ADMIN';
export type Status = 'NEW' | 'ASSIGNED' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';
export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
export interface User { id: number; name: string; email: string; role: Role; active: boolean; }
export interface Catalog { id: number; name: string; description: string; active: boolean; }
export interface Technician { id: number; userId: number; name: string; email: string; specialty: string; active: boolean; }
export interface ReportDraft { title: string; description: string; location: string; categoryId: number | null; areaId: number | null; priority: Priority; }
export interface Report extends ReportDraft { id: number; status: Status; categoryName: string; areaName: string; reporterId: number; reporterName: string; technicianId: number | null; technicianName: string | null; createdAt: string; updatedAt: string; resolvedAt: string | null; closedAt: string | null; history: {id:number;fromStatus:Status|null;toStatus:Status;note:string;actorName:string;createdAt:string}[]; notifications: {id:number;status:string;recipient:string;detail:string;createdAt:string}[]; }
export interface Comment { id:number; body:string; authorId:number; authorName:string; createdAt:string; }
export interface Suggestion { source: 'AI' | 'MANUAL'; title: string | null; summary: string | null; categoryId: number | null; priority: Priority | null; explanation: string; }
export interface Metric { label: string; count: number; }
export interface Dashboard { total:number;open:number;resolved:number;closed:number;averageResolutionHours:number|null;byStatus:Metric[];byCategory:Metric[];byArea:Metric[];byMonth:Metric[]; }
