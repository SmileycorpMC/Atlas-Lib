package net.smileycorp.atlas.api;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SimpleIntMessage implements IMessage {
		
		public SimpleIntMessage() {}
		
		private int value;
		
		public SimpleIntMessage(int value) {
			this.value=value;
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(value);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			value = buf.readInt();
		}
		
		public int getValue() {
			return value;
		}

}
