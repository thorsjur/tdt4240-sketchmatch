import { BaseDTO } from '../BaseDTO.mjs';

export class JoinGameByCodeRequest extends BaseDTO {

    // Properties declaration. If true, the property is required
    gameCode = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }

}