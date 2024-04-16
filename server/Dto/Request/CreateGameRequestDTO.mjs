import { BaseDTO } from '../BaseDTO.mjs';

export class CreateGameRequestDTO extends BaseDTO {

    // Properties declaration. If true, the property is required
    gameRoomName = true;
    roomCapacity = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }

}