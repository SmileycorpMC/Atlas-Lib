package net.smileycorp.atlas.api;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SimpleByteMessage implements IMessage {
		
		public SimpleByteMessage() {}
		
		private byte[] data;
		
		public SimpleByteMessage(byte[] data) {
			this.data=data;
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeBytes(data);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			data = new byte[buf.readableBytes()];
			buf.readBytes(data);
		}
		
		public byte[] getData() {
			return data;
		}

}
