export interface ApiResponse<T = any> {
    code: number;
    message: string;
    data: T;
}

export interface Task {
    id: number;
    title: string;
    rewardPoint: number;
    description: string;
    startTime: string | null;
    endTime: string | null;
    sortOrder: number;
    canDo: boolean;
}

export type TaskListResponse = ApiResponse<Task[]>;

export interface PointRecord {
    userId: number;
    changeType: string;
    changePoint: number;
    eventDescription: string;
    changeTime: string | null;
}

export type RecordListResponse = ApiResponse<PointRecord[]>;

export interface TodayPoint {
    userId: number;
    pointCount: number;
}

export type TodayPointResponse = ApiResponse<TodayPoint>;

export interface TotalPoint {
    userId: number;
    pointCount: number;
}

export type TotalPointResponse = ApiResponse<TotalPoint>;

export interface WithdrawRequest {
    amount: number;
    phone: string;
}

export interface IdCardInformation {
    name: string;
    idCardNum: string;
    idCardPic: string;
}

export type IdCardInformationResponse = ApiResponse<IdCardInformation>;

export interface EducationInformation {
    schoolName: string;
    certificatePic: string;
}

export type EducationInformationResponse = ApiResponse<EducationInformation>;

export interface WorkInformation {
    id: number;
    workYears: string;
    socialSecurityRecordPicUrl: string;
}

export type WorkInformationResponse = ApiResponse<WorkInformation>;

export interface CertificateInformation {
    id: number;
    certificateName: string;
    certificatePic: string;
}

export type CertificateInformationResponse = ApiResponse<CertificateInformation[]>;

export interface ResignInformation {
    id: number;
    joinAt: string;
    resignAt: string;
    resignReason: string;
    witnessName: string;
    witnessPhone: string;
}

export type ResignInformationResponse = ApiResponse<ResignInformation>;

export interface MatchJobItem {
    postId: number;
    postName: string;
    postTag: string[];
    createdAt: string;
    minSalary: string;
    maxSalary: string;
    postStatusDescs: string[];
    city: string;
    province: string;
    state: string;
    companyType: string;
    staffSize: number;
    companyName: number;
    companyLogoUrl: string;
    matchPercent: number;
}

export type JobInformationResponse = ApiResponse<MatchJobItem[]>;

export interface AiConclusion {
    conclusion: string,
    keywords: string[],
    score: number
}

export interface AiConclusionResponse {
    code: number;
    data: AiConclusion | null;
    msg?: string;
}

export interface BetterJobItem {
    postId: number;
    postName: string;
    postTag: string[];
    createdAt: string;
    minSalary: string;
    maxSalary: string;
    postStatusDescs: string[];
    city: string;
    province: string;
    state: string;
    companyType: string;
    staffSize: number;
    companyName: number;
    companyLogoUrl: string;
}

export interface DimensionResult {
    dimension: string;
    score: number;
    level: string;
    reason: string;
}

export interface TalentRatingResult {
    userId: number;
    overallScore: number;
    overallLevel: string;
    overallEvaluate: string;
    dimensionResults: DimensionResult[];
}

export type TalentRatingResponse = ApiResponse<TalentRatingResult>;