import { BaseDTO } from "../BaseDTO.mjs";

export class RoundTimerUpdateResponseDTO extends BaseDTO {
    // Properties declaration. If true, the property is required
    roundTimerTick = true;

    // Override the setProperties method
    setProperties(dataJson) {
        super.setProperties(this, dataJson);
    }
}
