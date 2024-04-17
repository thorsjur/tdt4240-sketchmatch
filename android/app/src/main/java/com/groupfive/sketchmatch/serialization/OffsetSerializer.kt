package com.groupfive.sketchmatch.serialization

import androidx.compose.ui.geometry.Offset
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


object OffsetSerializer : KSerializer<Offset> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Offset") {
        element("x", PrimitiveSerialDescriptor("x", PrimitiveKind.FLOAT))
        element("y", PrimitiveSerialDescriptor("y", PrimitiveKind.FLOAT))
    }

    override fun serialize(encoder: Encoder, value: Offset) {
        encoder.encodeStructure(descriptor) {
            encodeFloatElement(descriptor, 0, value.x)
            encodeFloatElement(descriptor, 1, value.y)
        }
    }

    override fun deserialize(decoder: Decoder): Offset = decoder.decodeStructure(descriptor) {
        var x: Float? = null
        var y: Float? = null

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> x = decodeFloatElement(descriptor, 0)
                1 -> y = decodeFloatElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }

        Offset(
            requireNotNull(x),
            requireNotNull(y)
        )
    }
}