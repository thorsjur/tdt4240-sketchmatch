package com.groupfive.sketchmatch.serialization

import androidx.compose.ui.graphics.Color
import io.ak1.drawbox.DrawBoxPayLoad
import io.ak1.drawbox.PathWrapper
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object DrawBoxPayLoadSerializer : KSerializer<DrawBoxPayLoad> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("DrawBoxPayLoad") {
        element("bgColor", ColorSerializer.descriptor)
        element("path", ListSerializer(PathWrapperSerializer).descriptor)
    }

    override fun serialize(encoder: Encoder, value: DrawBoxPayLoad) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, ColorSerializer, value.bgColor)
            encodeSerializableElement(
                descriptor,
                1,
                ListSerializer(PathWrapperSerializer),
                value.path
            )
        }
    }

    override fun deserialize(decoder: Decoder): DrawBoxPayLoad = decoder.decodeStructure(
        descriptor
    ) {
        var bgColor: Color? = null
        var path: List<PathWrapper>? = null

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> bgColor = decodeSerializableElement(descriptor, 0, ColorSerializer)
                1 -> path =
                    decodeSerializableElement(descriptor, 1, ListSerializer(PathWrapperSerializer))

                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }

        DrawBoxPayLoad(requireNotNull(bgColor), requireNotNull(path))
    }
}