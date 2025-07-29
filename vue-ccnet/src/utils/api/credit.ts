import request from '@/utils/request'
import { ApiResponse, CertificateInformationResponse, EducationInformationResponse, IdCardInformationResponse, ResignInformationResponse, WorkInformationResponse } from './type'

export function getCreditPoints(): Promise<ApiResponse<number>> {
    return request.get('/credit')
}

export function getIdCardInformation(): Promise<IdCardInformationResponse> {
    return request.get('/idcards')
}

export function saveIdCardInformation(
    name: string,
    idCardNum: string,
    idCardImage: File
): Promise<ApiResponse> {
    const formData = new FormData();
    formData.append('idCardNum', idCardNum);
    formData.append('name', name);
    formData.append('idCardImage', idCardImage);
    return request.post('/idcards', formData)
}

export function deleteIdCardInformation(): Promise<ApiResponse> {
    return request.delete('/idcards')
}

export function getPhoneNumber(): Promise<ApiResponse<number>> {
    return request.get('/idcards')
}

export function getEducations(): Promise<EducationInformationResponse> {
    return request.get('/educations')
}

export function saveEducationInformation(
    schoolName: string,
    certificateImage: File
): Promise<ApiResponse> {
    const formData = new FormData();
    formData.append('schoolName', schoolName);
    formData.append('certificateImage', certificateImage);
    return request.post('/educations', formData)
}

export function deleteEducationInformation(): Promise<ApiResponse> {
    return request.delete('/educations')
}

export function getWorkInformation(): Promise<WorkInformationResponse> {
    return request.get('/work-experiences')
}

export function saveWorkInformation(
    workYears: string,
    socialSecurityImage: File
): Promise<ApiResponse> {
    const formData = new FormData();
    formData.append('workYears', String(workYears));
    formData.append('socialSecurityImage', socialSecurityImage);
    return request.post('/work-experiences', formData)
}

export function deleteWorkInformation(): Promise<ApiResponse> {
    return request.delete('/work-experiences')
}

export function getCertificateInformation(): Promise<CertificateInformationResponse> {
    return request.get('/certificates')
}

export function saveCertificateInformation(
    certificateName: string,
    certificateImage: File
): Promise<ApiResponse> {
    const formData = new FormData();
    formData.append('certificateName', certificateName);
    formData.append('certificateImage', certificateImage);
    return request.post('/certificates', formData)
}

export function getResignInformation(): Promise<ResignInformationResponse> {
    return request.get('/resignations')
}

export function saveResignInformation(
    joinAt: string,
    resignAt: string,
    resignReason: string,
    witnessName: string,
    witnessPhone: string
): Promise<ApiResponse> {
    const formData = new FormData();
    formData.append('resignReason', resignReason);
    formData.append('joinAt', joinAt);
    formData.append('resignAt', resignAt);
    formData.append('witnessName', witnessName);
    formData.append('witnessPhone', witnessPhone);
    return request.post('/resignations', formData)
}

export function deleteResignInformation(): Promise<ApiResponse> {
    return request.delete('/resignations')
}

export function getUsername(): Promise<ApiResponse> {
    return request.get('/user/name')
}

