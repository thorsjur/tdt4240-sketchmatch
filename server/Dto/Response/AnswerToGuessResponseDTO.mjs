import { BaseDTO } from "../BaseDTO.mjs";

export class AnswerToGuessResponseDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    isCorrect = true;
    playerId = true;
    gameRoom = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}
