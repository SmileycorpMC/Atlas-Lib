package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class SimpleStringMessage<T extends PacketListener> implements Packet<T> {

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
		public void handle(T listener) {
			
		}


}
