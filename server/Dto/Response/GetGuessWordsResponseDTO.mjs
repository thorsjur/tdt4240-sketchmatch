import { BaseDTO } from '../BaseDTO.mjs';

export class GetGuessWordsResponseDTO extends BaseDTO {

    // Properties declaration. If true, the property is required
    status = "success";
    message = "Guess words retrieved successfully";
    guessWords;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }

}