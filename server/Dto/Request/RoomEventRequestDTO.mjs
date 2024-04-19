import { BaseDTO } from "../BaseDTO.mjs";

export class RoomEventRequestDTO extends BaseDTO {
  // Properties declaration. If true, the property is required
  roomId = true;

  // Override the setProperties method
  setProperties(dataJson) {
    super.setProperties(this, dataJson);
  }
}
