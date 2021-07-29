package net.smileycorp.atlas.api;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SimpleStringMessage implements IMessage {
		
		public SimpleStringMessage() {}
		
		private String text;
		
		public SimpleStringMessage(String text) {
			this.text=text;
		}

		@Override
		public void toBytes(ByteBuf buf) {
			ByteBufUtils.writeUTF8String(buf, text);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			text = ByteBufUtils.readUTF8String(buf);
		}
		
		public String getText() {
			return text;
		}

}
