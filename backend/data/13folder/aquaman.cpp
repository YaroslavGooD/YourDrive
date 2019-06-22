#include <iostream>
#include <vector>
#include <cmath>
# define M_PI           3.14159265358979323846

using namespace std;

long double initialArea(vector<long double> x, vector<long double> y, int n)
{
	long double area = 0;
	int j = n - 1;
	for (int i = 0; i < n; i++)
	{
		area += (x[j] + x[i]) * (y[j] - y[i]);
		j = i;
	}
	return abs(area / 2.0);
}

long double extraArea1(vector<long double> x, vector<long double> y, int n) {
	long double area = 0;
	int j = n - 1;
	for (int i = 0; i < n; i++)
	{
		area += sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
		j = i;
	}
	return area;
}

long double extraArea2(int k) {
	return M_PI * k * k;
}

int main()
{
	int t;
	cin >> t;
	while (t-- > 0) {
		int n, k;
		scanf("%i", &n);
		scanf("%i", &k);
		vector<long double> x(n);
		vector<long double> y(n);
		for (int i = 0; i < n; i++)
		{
			scanf("%Lf", &x[i]);
			scanf("%Lf", &y[i]);
		}
		long double result = initialArea(x, y, n) + extraArea1(x, y, n)*k + extraArea2(k);
		printf("%Lf\n", result);
	}
	return 0;
}