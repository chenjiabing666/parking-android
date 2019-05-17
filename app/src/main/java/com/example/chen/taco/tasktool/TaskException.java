package com.example.chen.taco.tasktool;

public class TaskException extends Exception {

		private String info;
		
		public TaskException(String info) {
			this.info = info;
		}

		private static final long serialVersionUID = 1L;
		
		@Override
		public String toString() {
			return info;
		}

	}
