package net.smileycorp.atlas.api;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import org.apache.commons.lang3.ArrayUtils;

public class SimpleStringMessage implements IMessage {
		
		public SimpleStringMessage() {}
		
		private String text;
		
		public SimpleStringMessage(String text) {
			this.text=text;
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeBytes(text.getBytes());
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			byte[] data = new byte[buf.readableBytes()];
			buf.readBytes(data);
			byte[] bytes = {};
			for (byte b : data) {
				if (b != 0x0) bytes = ArrayUtils.add(bytes, b);
			}
			text = new String(bytes);
		}
		
		public String getText() {
			return text;
		}

}
