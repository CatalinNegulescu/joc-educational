

// eslint-disable-next-line @typescript-eslint/no-empty-interface
export interface Question {
    questionText: string,
    options: string[],
    correctOption: string
}

export interface QuestionModel {
    text: string,
    variantaRaspunsA: string,
    variantaRaspunsB: string,
    variantaRaspunsC: string,
    variantaRaspunsD: string,
    variantaRaspunsCorect: string,
}

export interface QuestionCheck {
    questionText: string,
    options: string[],
    correctOption: string,
    itWasMarkedAsCorrect: boolean,
    lastOptionSelected: string
}

export interface Score {
    classId: string;
    score: string;
    testTimeInSec: string;
}
