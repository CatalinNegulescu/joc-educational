export interface Class {
    id: string;
    description: string;
}

export interface QuestionSave {
    classId: string;
    question: string;
    questionResponse1: string;
    questionResponse2: string;
    questionResponse3: string;
    questionResponse4: string;
    questionValidResponse: string;
}