package Tamaized.AoV.gui.client;

import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import Tamaized.AoV.AoV;
import Tamaized.AoV.common.client.ClientProxy;
import Tamaized.AoV.common.handlers.ServerPacketHandler;
import Tamaized.AoV.core.AoVData;
import Tamaized.AoV.core.abilities.AbilityBase;
import Tamaized.AoV.core.abilities.universal.InvokeMass;
import Tamaized.AoV.core.skills.AoVSkill;
import Tamaized.AoV.gui.GuiHandler;
import Tamaized.AoV.gui.buttons.BlankButton;
import Tamaized.AoV.gui.buttons.SpellButton;

public class SpellBookGUI extends GuiScreen {
	
	private static final int BUTTON_CLOSE = 0;
	private static final int BUTTON_BACK = 1;
	
	public static final int BUTTON_SPELL = 2;

	public static final int BUTTON_BAR_SLOT_0 = 3;
	public static final int BUTTON_BAR_SLOT_1 = 4;
	public static final int BUTTON_BAR_SLOT_2 = 5;
	public static final int BUTTON_BAR_SLOT_3 = 6;
	public static final int BUTTON_BAR_SLOT_4 = 7;
	public static final int BUTTON_BAR_SLOT_5 = 8;
	public static final int BUTTON_BAR_SLOT_6 = 9;
	public static final int BUTTON_BAR_SLOT_7 = 10;
	public static final int BUTTON_BAR_SLOT_8 = 11;

	@Override
	public void initGui(){
		int margin = 20;
		int padding = 100;
		float workW = width-padding;
		int loc1 = (int) (workW*.25) + margin;
		int loc2 = (int) (workW*.75) + margin;
		buttonList.add(new GuiButton(BUTTON_BACK, loc1, height-25, 80, 20, "Back"));
		buttonList.add(new GuiButton(BUTTON_CLOSE, loc2, height-25, 80, 20, "Close"));
		
		int id = 0;
		int xLoc = 50;
		int yLoc = 50;
		
		ArrayList<String> spellList = new ArrayList<String>();
		for(AoVSkill skill : AoV.clientAoVCore.getPlayer(null).getAllObtainedSkills()){
			for(String s : skill.abilities){
				AbilityBase spell = AbilityBase.fromName(s);
				if(spellList.contains(s) || spell == null) continue;
				spellList.add(s);
				buttonList.add(new SpellButton(BUTTON_SPELL, xLoc, yLoc+(25*(id)), spell));
				id++;
			}
		}
		spellList.clear();
		spellList = null;
		{
			int y = height-47;
			int x = width/2;
			buttonList.add(new BlankButton(BUTTON_BAR_SLOT_0, x-88, y, 16, 16, false));
			buttonList.add(new BlankButton(BUTTON_BAR_SLOT_1, x-68, y, 16, 16, false));
			buttonList.add(new BlankButton(BUTTON_BAR_SLOT_2, x-48, y, 16, 16, false));
			buttonList.add(new BlankButton(BUTTON_BAR_SLOT_3, x-28, y, 16, 16, false));
			buttonList.add(new BlankButton(BUTTON_BAR_SLOT_4, x-8, y, 16, 16, false));
			buttonList.add(new BlankButton(BUTTON_BAR_SLOT_5, x+12, y, 16, 16, false));
			buttonList.add(new BlankButton(BUTTON_BAR_SLOT_6, x+32, y, 16, 16, false));
			buttonList.add(new BlankButton(BUTTON_BAR_SLOT_7, x+52, y, 16, 16, false));
			buttonList.add(new BlankButton(BUTTON_BAR_SLOT_8, x+72, y, 16, 16, false));
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException{
		if (button.enabled){
			switch(button.id){
				case BUTTON_CLOSE:
					mc.displayGuiScreen((GuiScreen)null);
					break;
				case BUTTON_BACK:
					GuiHandler.openGUI(GuiHandler.GUI_SKILLS);
					break;
				case BUTTON_SPELL:
					this.sendPacketTypeAddNearestSlot(((SpellButton) button).spell.getName());
					break;
				case BUTTON_BAR_SLOT_0:
					sendPacketTypeRemoveSlot(0);
					break;
				case BUTTON_BAR_SLOT_1:
					sendPacketTypeRemoveSlot(1);
					break;
				case BUTTON_BAR_SLOT_2:
					sendPacketTypeRemoveSlot(2);
					break;
				case BUTTON_BAR_SLOT_3:
					sendPacketTypeRemoveSlot(3);
					break;
				case BUTTON_BAR_SLOT_4:
					sendPacketTypeRemoveSlot(4);
					break;
				case BUTTON_BAR_SLOT_5:
					sendPacketTypeRemoveSlot(5);
					break;
				case BUTTON_BAR_SLOT_6:
					sendPacketTypeRemoveSlot(6);
					break;
				case BUTTON_BAR_SLOT_7:
					sendPacketTypeRemoveSlot(7);
					break;
				case BUTTON_BAR_SLOT_8:
					sendPacketTypeRemoveSlot(8);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	

	@Override
	public void onGuiClosed(){
		
	}
	

	@Override
	public void updateScreen(){
		
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, "Angel of Vengeance: SpellBook", this.width / 2, 15, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderBar(partialTicks);
		for(GuiButton b : buttonList){
			if(!b.isMouseOver()) continue;
			if(b instanceof SpellButton){
				SpellButton sb = (SpellButton) b;
				if(sb.spell != null && sb.spell.description != null) this.drawHoveringText(sb.spell.description, mouseX, mouseY);
			}
		}
	}
	
	private void renderBar(float partialTicks){
		AoVData data = AoV.clientAoVCore.getPlayer(null);
		ScaledResolution sr = new ScaledResolution(mc);
		float alpha = 1.0f;
		GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
		mc.getTextureManager().bindTexture(AoVUIBar.widgetsTexPath);
		int i = sr.getScaledWidth() / 2;
		this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 50, 0, 0, 182, 22);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.01f, 0, 0);
		GlStateManager.translate(-20.01f, 0, 0);
		for (int j = 0; j < 9; ++j){
			GlStateManager.translate(20.01f, 0, 0);
			if(data.getSlot(j) == null) continue;
			int k = sr.getScaledWidth() / 2 - 90 + 2;
			int l = sr.getScaledHeight() - 47;
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);	
			AoVUIBar.renderHotbarIcon(this, null, j, k, l, partialTicks, data.getSlot(j).getIcon(), (data.getSlot(j) instanceof InvokeMass) ? data.invokeMass : false);
		}
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}
	
	private void sendPacketTypeRemoveSlot(int slot){
		ByteBufOutputStream bos = new ByteBufOutputStream(Unpooled.buffer());
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(ServerPacketHandler.TYPE_SPELLBAR_REMOVE);
			outputStream.writeInt(slot);
			FMLProxyPacket pkt = new FMLProxyPacket(new PacketBuffer(bos.buffer()), AoV.networkChannelName);
			if(AoV.channel != null && pkt != null) AoV.channel.sendToServer(pkt);
			bos.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		sendChargeUpdates();
	}
	
	private void sendPacketTypeAddNearestSlot(String ability){
		ByteBufOutputStream bos = new ByteBufOutputStream(Unpooled.buffer());
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(ServerPacketHandler.TYPE_SPELLBAR_ADDNEAR);
			outputStream.writeUTF(ability);
			FMLProxyPacket pkt = new FMLProxyPacket(new PacketBuffer(bos.buffer()), AoV.networkChannelName);
			if(AoV.channel != null && pkt != null) AoV.channel.sendToServer(pkt);
			bos.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		sendChargeUpdates();
	}
	
	private void sendChargeUpdates(){
		ByteBufOutputStream bos = new ByteBufOutputStream(Unpooled.buffer());
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(ServerPacketHandler.TYPE_CHARGES_CONFIGURE);
			FMLProxyPacket pkt = new FMLProxyPacket(new PacketBuffer(bos.buffer()), AoV.networkChannelName);
			if(AoV.channel != null && pkt != null) AoV.channel.sendToServer(pkt);
			bos.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
