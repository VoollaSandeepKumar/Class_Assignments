class Solution {
	public int solution(int[] A, int X, int D) {
		// write your code in Java SE 8
		int[] leafWithFallingTime = new int[X];
		int minTimeToJump = 0;
		int temp = Integer.MAX_VALUE;
		// declaring the array with Integer.MAX_VALUE initially to identify the leafs that are
		// not falling.
		for (int i = 0; i < leafWithFallingTime.length; i++) {
			leafWithFallingTime[i] = Integer.MAX_VALUE;
		}
		// Given the array with index representing the time when the leaf falls.
		// converting the input to a new array with index representing the leaf
		// and its falling time.
		for (int i = 0; i < A.length; i++) {
			if(leafWithFallingTime[A[i]]==Integer.MAX_VALUE)
			{
				leafWithFallingTime[A[i]] = i;
			}
			if(leafWithFallingTime[A[i]]!= Integer.MAX_VALUE && leafWithFallingTime[i]< i)
			leafWithFallingTime[A[i]] = i;
		}
		// condition to check if frog jumps in a single hop.
		if (X <= D) {
			return 0;
		}
		for (int i = 1; i < leafWithFallingTime.length; i++) {
			int k =i;
			if(i-1+D >= X)
			{
				return minTimeToJump;
			}
			temp= Integer.MAX_VALUE;
			for (int j = k; j < i+D; j++) {
				if(j< leafWithFallingTime.length)
				{
				if(leafWithFallingTime[j]<temp)
				{
				temp = leafWithFallingTime[j];
				if(temp>minTimeToJump)
				minTimeToJump=temp;
				i = j;
				}
				
			}
				else
				{
					if(i+D >= X)
					{
						return minTimeToJump;
					}
					else
					{
						return -1;
					}
				}
				
				
			}
		}
		return minTimeToJump;
	}

	public static void main(String[] args) {
		Solution frogjumpSolution = new Solution();
		int[] leafTiming = { 10,8,6,4,2};
		int lengthOfPond = 12;
		int maxFrogJump = 3;
		int minTime = frogjumpSolution.solution(leafTiming, lengthOfPond, maxFrogJump);
		System.out.println(minTime);

	}

}