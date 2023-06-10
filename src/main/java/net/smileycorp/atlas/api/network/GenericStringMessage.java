package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraftforge.network.NetworkEvent;


public class GenericStringMessage extends AbstractMessage {

	public GenericStringMessage() {}

	private String text;

	public GenericStringMessage(String text) {
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

	@Override
	public void process(NetworkEvent.Context ctx) {
		throw new IllegalArgumentException("Please use the other register method, when using generic messages!");
	}
		
}
