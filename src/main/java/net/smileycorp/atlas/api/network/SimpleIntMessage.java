package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;


public class SimpleIntMessage extends SimpleAbstractMessage {

		public SimpleIntMessage() {}

		private int value;

		public SimpleIntMessage(int value) {
			this.value=value;
		}

		public int get() {
			return value;
		}

		@Override
		public void write(FriendlyByteBuf buf) {
			buf.writeInt(value);
		}
		
		@Override
		public void read(FriendlyByteBuf buf) {
			value = buf.readInt();
		}

		@Override
		public void handle(PacketListener listener) {}

}
