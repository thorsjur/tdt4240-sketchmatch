export class BaseDTO {
    constructor() {
        
    }

    // Add the properties to the object
    setProperties(childContext, dataJson) {
        var listOfFields = Object.getOwnPropertyNames(childContext);
        // Validate properties
        this.validateProperties(listOfFields, dataJson);

        for (var key in dataJson) {
            this[key] = dataJson[key];
        }
    }

    // Properties validator
    validateProperties(properties, dataJson) {
        var missingProperties = [];
        
        // Check if there are required properties that are not in the json
        for (var i = 0; i < properties.length; i++) {
            if (properties[i] == true && !(properties[i] in dataJson)) {
                missingProperties.push(properties[i]);
            }
        }

        // Check if there are json properties that are not in the DTO Object properties
        for (var key in dataJson) {
            if (!properties.includes(key)) {
                throw new Error(`Json property ${key} is not in the DTO object.`);
            }
        }

        if (missingProperties.length > 0) {
            throw new Error(`${missingProperties.length} Missing properties: ${missingProperties}`);
        }
    }
}