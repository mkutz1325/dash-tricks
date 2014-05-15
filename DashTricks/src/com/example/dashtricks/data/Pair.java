package com.example.dashtricks.data;


/**
 * this class define pair structure
 * @author Chelsea
 *
 * @param <T>
 * @param <E>
 */
public class Pair<T, E> {

		private T first;
		private E second;
		
		public Pair(T f, E s){
			first = f;
			second = s;
		}
		
		/**
		 * get first element in the pair
		 * @return first
		 */
		public T getFirst(){
			return first;
		}
		
		/**
		 * get second element in the pair
		 * @return second
		 */
		public E getSecond(){
			return second;
		}
		
		public String toString() {
			return first + ", " + second;
		}
		
}


