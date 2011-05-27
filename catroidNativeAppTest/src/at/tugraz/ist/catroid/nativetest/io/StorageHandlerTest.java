/**
  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team 
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.tugraz.ist.catroid.nativetest.io;

import java.io.IOException;

import android.test.InstrumentationTestCase;
import at.tugraz.ist.catroid.content.Project;
import at.tugraz.ist.catroid.content.Sprite;
import at.tugraz.ist.catroid.content.StartScript;
import at.tugraz.ist.catroid.content.TapScript;
import at.tugraz.ist.catroid.content.bricks.ChangeXByBrick;
import at.tugraz.ist.catroid.content.bricks.ChangeYByBrick;
import at.tugraz.ist.catroid.content.bricks.ComeToFrontBrick;
import at.tugraz.ist.catroid.content.bricks.GlideToBrick;
import at.tugraz.ist.catroid.content.bricks.GoNStepsBackBrick;
import at.tugraz.ist.catroid.content.bricks.HideBrick;
import at.tugraz.ist.catroid.content.bricks.PlaceAtBrick;
import at.tugraz.ist.catroid.content.bricks.PlaySoundBrick;
import at.tugraz.ist.catroid.content.bricks.ScaleCostumeBrick;
import at.tugraz.ist.catroid.content.bricks.SetCostumeBrick;
import at.tugraz.ist.catroid.content.bricks.SetXBrick;
import at.tugraz.ist.catroid.content.bricks.SetYBrick;
import at.tugraz.ist.catroid.content.bricks.ShowBrick;
import at.tugraz.ist.catroid.content.bricks.WaitBrick;
import at.tugraz.ist.catroid.io.StorageHandler;
import at.tugraz.ist.catroid.stage.NativeAppActivity;

public class StorageHandlerTest extends InstrumentationTestCase {
	private StorageHandler storageHandler;

	public StorageHandlerTest() throws IOException {
		storageHandler = StorageHandler.getInstance();
	}
	
	public void testLoadProject() throws Exception {
		double scaleValue = 0.8;
		int timeToWaitInMilliSeconds = 1000;
		int xPosition = 100;
		int yPosition = 100;
		int xMovement = -100;
		int yMovement = -100;
		int durationInMilliSeconds = 3000;
		int xDestination = 500;
		int yDestination = 500;
		int steps = 1;
				
		Project testProject = new Project(getInstrumentation().getTargetContext(), "testProject");
		Sprite firstSprite = new Sprite("first");
		Sprite secondSprite = new Sprite("second");
		StartScript firstStartScript = new StartScript("testScript", firstSprite);
		TapScript firstTapScript = new TapScript("script", firstSprite);
		StartScript secondStartScript = new StartScript("otherScript", secondSprite);
		
		HideBrick hideBrick = new HideBrick(firstSprite);
		ShowBrick showBrick = new ShowBrick(firstSprite);
		ScaleCostumeBrick scaleCostumeBrick = new ScaleCostumeBrick(firstSprite, scaleValue);
		ComeToFrontBrick comeToFrontBrick = new ComeToFrontBrick(firstSprite);
		SetCostumeBrick setCostumeBrick = new SetCostumeBrick(firstSprite);
		
		WaitBrick waitBrick = new WaitBrick(firstSprite, timeToWaitInMilliSeconds);
		PlaySoundBrick playSoundBrick = new PlaySoundBrick(firstSprite);	
		
		PlaceAtBrick placeAtBrick = new PlaceAtBrick(secondSprite, xPosition, yPosition);
		SetXBrick setXBrick = new SetXBrick(secondSprite, xPosition);
		SetYBrick setYBrick = new SetYBrick(secondSprite, yPosition);
		ChangeXByBrick changeXByBrick = new ChangeXByBrick(secondSprite, xMovement);
		ChangeYByBrick changeYByBrick = new ChangeYByBrick(secondSprite, yMovement);
		GlideToBrick glideToBrick = new GlideToBrick(secondSprite, xDestination, yDestination, durationInMilliSeconds);
		GoNStepsBackBrick goNStepsBackBrick = new GoNStepsBackBrick(secondSprite, steps);
     
		
		firstStartScript.addBrick(hideBrick);
		firstStartScript.addBrick(showBrick);
		firstStartScript.addBrick(scaleCostumeBrick);
		firstStartScript.addBrick(comeToFrontBrick);
		firstStartScript.addBrick(setCostumeBrick);
		
		firstTapScript.addBrick(waitBrick);
		firstTapScript.addBrick(playSoundBrick);
		
		secondStartScript.addBrick(placeAtBrick);
		secondStartScript.addBrick(setXBrick);
		secondStartScript.addBrick(setYBrick);
		secondStartScript.addBrick(changeXByBrick);
		secondStartScript.addBrick(changeYByBrick);
		secondStartScript.addBrick(glideToBrick);
		secondStartScript.addBrick(goNStepsBackBrick);
		
		
		firstSprite.getScriptList().add(firstStartScript);
		firstSprite.getScriptList().add(firstTapScript);
		secondSprite.getScriptList().add(secondStartScript);
		
		
		testProject.addSprite(firstSprite);
		testProject.addSprite(secondSprite);
		

		NativeAppActivity.setContext(getInstrumentation().getContext());
		Project loadedProject = storageHandler.loadProject("test_project");
		
		assertEquals("Project title missmatch.", testProject.getName(), loadedProject.getName());
		
		assertEquals("Name of first sprite does not match.", testProject.getSpriteList().get(0).getName(),
				loadedProject.getSpriteList().get(0).getName());
		assertEquals("Name of second sprite does not match.", testProject.getSpriteList().get(1).getName(),
				loadedProject.getSpriteList().get(1).getName());
		assertEquals("Name of third sprite does not match.", testProject.getSpriteList().get(2).getName(),
				loadedProject.getSpriteList().get(2).getName());

		
		assertEquals("HideBrick was not loaded right", HideBrick.class, loadedProject.getSpriteList().get(1).getScriptList().get(0).getBrickList().get(0).getClass());
		assertEquals("ShowBrick was not loaded right", ShowBrick.class, loadedProject.getSpriteList().get(1).getScriptList().get(0).getBrickList().get(1).getClass());
		assertEquals("ScaleBrick was not loaded right", scaleValue,
				((ScaleCostumeBrick) (loadedProject.getSpriteList().get(1).getScriptList().get(0).getBrickList().get(2))).getScale());
		assertEquals("ComeToFrontBrick was not loaded right", ComeToFrontBrick.class, loadedProject.getSpriteList().get(1).getScriptList().get(0).getBrickList().get(3).getClass());
		assertEquals("SetCostumeBrick was not loaded right", SetCostumeBrick.class, loadedProject.getSpriteList().get(1).getScriptList().get(0).getBrickList().get(4).getClass());
		
		assertEquals("WaitBrick was not loaded right", timeToWaitInMilliSeconds,
				((WaitBrick) (loadedProject.getSpriteList().get(1).getScriptList().get(1).getBrickList().get(0))).getWaitTime());
		assertEquals("PlaySoundBrick was not loaded right", PlaySoundBrick.class, loadedProject.getSpriteList().get(1).getScriptList().get(1).getBrickList().get(1).getClass());

		assertEquals("PlaceAtBrick was not loaded right", xPosition,
				((PlaceAtBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(0))).getXPosition());
		assertEquals("PlaceAtBrick was not loaded right", yPosition,
				((PlaceAtBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(0))).getYPosition());
		assertEquals("SetXBrick was not loaded right", xPosition,
				((SetXBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(1))).getXPosition());
		assertEquals("SetYBrick was not loaded right", yPosition,
				((SetYBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(2))).getYPosition());
		assertEquals("ChangeXByBrick was not loaded right", xMovement,
				((ChangeXByBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(3))).getXMovement());
		assertEquals("ChangeYByBrick was not loaded right", yMovement,
				((ChangeYByBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(4))).getYMovement());
		assertEquals("GlideToBrick was not loaded right", xDestination,
				((GlideToBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(5))).getXDestination());
		assertEquals("GlideToBrick was not loaded right", yDestination,
				((GlideToBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(5))).getYDestination());
		assertEquals("GlideToBrick was not loaded right", durationInMilliSeconds,
				((GlideToBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(5))).getDurationInMilliSeconds());
		assertEquals("GoNStepsBackBrick was not loaded right", steps,
				((GoNStepsBackBrick) (loadedProject.getSpriteList().get(2).getScriptList().get(0).getBrickList().get(6))).getSteps());
		
	}
}
