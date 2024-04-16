import { BaseDTO } from '../BaseDTO.mjs';

export class CreateGameResponseDTO extends BaseDTO {

    // Properties declaration. If true, the property is required
    status = "success";
    message = "Game room created successfully";
    gameRoom;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }

}