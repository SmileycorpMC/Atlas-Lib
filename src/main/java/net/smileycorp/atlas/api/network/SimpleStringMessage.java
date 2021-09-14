package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;


public class SimpleStringMessage extends SimpleAbstractMessage {

		public SimpleStringMessage() {}

		private String text;

		public SimpleStringMessage(String text) {
			this.text=text;
		}

		public String getText() {
			return text;
		}

		@Override
		public void write(FriendlyByteBuf buf) {
			buf.writeUtf(text);
		}

		@Override
		public void read(FriendlyByteBuf buf) {
			text = buf.readUtf();
		}

		@Override
		public void handle(PacketListener listener) {}
		
}
