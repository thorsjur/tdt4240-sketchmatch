import { BaseDTO } from "../BaseDTO.mjs";

export class PublishPathRequestDTO extends BaseDTO {
  // Properties declaration. If true, the property is required
  roomId = true;
  pathPayload = true;

  // Override the setProperties method
  setProperties(dataJson) {
    super.setProperties(this, dataJson);
  }
}
