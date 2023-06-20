package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {

	public static void main(String[] args) {
		
		ObjectStack stack = new ObjectStack();
		
		String input = args[0];
		
		String[] input_chars = input.split("\\s+");

		for(String s: input_chars) {
			try {
				int num = Integer.parseInt(s);
				stack.push(num);
			} catch(NumberFormatException e) {
				int first = (int) stack.pop();
				int second = (int) stack.pop();



				int res = 0;
				if(s.equals("+")) {
					res = second + first;
				} else if(s.equals("-")) {
					res = second - first;
				} else if(s.equals("*")) {
					res = second * first;
				} else if(s.equals("/")) {
					try {
						res = (int) (second / first);
					} catch(ArithmeticException exc) {
						System.out.println("Cannot divide by 0");
						System.exit(0);
					}
					
				} else if (s.equals("bigger")) {
					if (second >= first) {
						res = second;
					} else {
						res = first;
					}
				} else if (s.equals("cubed")) {
					res = (int) Math.pow(first, 3);
					stack.push(second);
				} else {
					res = second % first;
				}
								
				
				stack.push(res);
			}
		}
		if(stack.size() != 1) {
			System.err.println("Wrong input");
		} else {
			System.out.println(stack.pop());
		}

	}

}
