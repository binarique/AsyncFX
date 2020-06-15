/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionwireless.asyncfx;

/**
 * @author walulya francis
 */
public class UploadLock {
private int RunningThreadNumber;

		public UploadLock() {
		this.RunningThreadNumber = 0;
		}

		public int getRunningThread() {
		return RunningThreadNumber;
		}

		public void registerThread() {
		RunningThreadNumber++;
		}

		public void removeThread() {
		RunningThreadNumber--;
		}   
}
