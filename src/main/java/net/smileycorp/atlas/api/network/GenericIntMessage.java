package net.smileycorp.atlas.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class GenericIntMessage implements NetworkMessage {

	public GenericIntMessage() {}

	private int value;

	public GenericIntMessage(int value) {
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
	public void process(IPayloadContext ctx) {
		throw new IllegalArgumentException("Please use the other register method, when using generic messages!");
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return null;
	}
	
}
