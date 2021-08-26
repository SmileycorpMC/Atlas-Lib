package net.smileycorp.atlas.api.network;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class SimpleStringMessage implements IPacket<INetHandler> {

		public SimpleStringMessage() {}

		private String text;

		public SimpleStringMessage(String text) {
			this.text=text;
		}

		public String getText() {
			return text;
		}

		@Override
		public void read(PacketBuffer buf) throws IOException {
			text = buf.readUtf();
		}

		@Override
		public void write(PacketBuffer buf) throws IOException {
			buf.writeUtf(text);
		}

		@Override
		public void handle(INetHandler handler) {}


}
