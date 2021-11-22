package com.reyzerbit.mca_reborn.network;

import java.util.function.Supplier;

import com.reyzerbit.mca_reborn.network.MCAMessages.BabyName;
import com.reyzerbit.mca_reborn.network.MCAMessages.ButtonAction;
import com.reyzerbit.mca_reborn.network.MCAMessages.CallToPlayer;
import com.reyzerbit.mca_reborn.network.MCAMessages.CareerRequest;
import com.reyzerbit.mca_reborn.network.MCAMessages.CareerResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.GetFamily;
import com.reyzerbit.mca_reborn.network.MCAMessages.GetFamilyResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.InventoryRequest;
import com.reyzerbit.mca_reborn.network.MCAMessages.InventoryResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.ReviveVillager;
import com.reyzerbit.mca_reborn.network.MCAMessages.SavedVillagersRequest;
import com.reyzerbit.mca_reborn.network.MCAMessages.SavedVillagersResponse;
import com.reyzerbit.mca_reborn.network.MCAMessages.Say;
import com.reyzerbit.mca_reborn.network.MCAMessages.SetName;
import com.reyzerbit.mca_reborn.network.MCAMessages.SetProfession;
import com.reyzerbit.mca_reborn.network.MCAMessages.SetTexture;
import com.reyzerbit.mca_reborn.network.MCAMessages.SpawnParticles;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageHandler {
	
	public static class Client {
		
		public static void handle(SpawnParticles msg, Supplier<NetworkEvent.Context> context) {
			
			// Client side handles must be wrapped inside a DistExecutor
		    context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handleMessage(msg, context.get().getSender())));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(Say msg, Supplier<NetworkEvent.Context> context) {
			
			// Client side handles must be wrapped inside a DistExecutor
		    context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handleMessage(msg, context.get().getSender())));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(CareerResponse msg, Supplier<NetworkEvent.Context> context) {
			
			// Client side handles must be wrapped inside a DistExecutor
		    context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handleMessage(msg, context.get().getSender())));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(InventoryResponse msg, Supplier<NetworkEvent.Context> context) {
			
			// Client side handles must be wrapped inside a DistExecutor
		    context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handleMessage(msg, context.get().getSender())));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(SavedVillagersResponse msg, Supplier<NetworkEvent.Context> context) {
			
			// Client side handles must be wrapped inside a DistExecutor
		    context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handleMessage(msg, context.get().getSender())));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(GetFamilyResponse msg, Supplier<NetworkEvent.Context> context) {
			
			// Client side handles must be wrapped inside a DistExecutor
		    context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handleMessage(msg, context.get().getSender())));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
	}
	
	public static class Server {
		
		public static void handle(ButtonAction msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}

		public static void handle(BabyName msg, Supplier<NetworkEvent.Context> context) {
			
		    context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}

		public static void handle(CareerRequest msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}

		public static void handle(InventoryRequest msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(SavedVillagersRequest msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(ReviveVillager msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(SetName msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(GetFamily msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(CallToPlayer msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(SetTexture msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
		public static void handle(SetProfession msg, Supplier<NetworkEvent.Context> context) {
			
			context.get().enqueueWork(() -> ServerHandler.processMessage(msg, context.get().getSender()));
		    
		    context.get().setPacketHandled(true);
		    
		}
		
	}

}
