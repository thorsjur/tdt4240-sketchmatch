package com.groupfive.sketchmatch.serialization

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object ColorSerializer : KSerializer<Color> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Color") {
        element("red", PrimitiveSerialDescriptor("red", PrimitiveKind.FLOAT))
        element("green", PrimitiveSerialDescriptor("green", PrimitiveKind.FLOAT))
        element("blue", PrimitiveSerialDescriptor("blue", PrimitiveKind.FLOAT))
        element(
            "alpha",
            PrimitiveSerialDescriptor("alpha", PrimitiveKind.FLOAT)
        )
    }

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeStructure(descriptor) {
            encodeFloatElement(descriptor, 0, value.red)
            encodeFloatElement(descriptor, 1, value.green)
            encodeFloatElement(descriptor, 2, value.blue)
            encodeFloatElement(descriptor, 3, value.alpha)
        }
    }

    override fun deserialize(decoder: Decoder): Color = decoder.decodeStructure(descriptor) {
        var red = 0f
        var green = 0f
        var blue = 0f
        var alpha = 1.0f

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> red = decodeFloatElement(descriptor, 0)
                1 -> green = decodeFloatElement(descriptor, 1)
                2 -> blue = decodeFloatElement(descriptor, 2)
                3 -> alpha = decodeFloatElement(descriptor, 2)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }
        
        Color(red, green, blue, alpha)
    }

}