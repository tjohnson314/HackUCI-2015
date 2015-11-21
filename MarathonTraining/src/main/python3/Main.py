import numpy as np


import collections
import itertools
import math
import sys
import time


BANANAS_RANGE = range(19)
MAX_CARBS_STORABLE = 500
STORAGE_RANGE = range(MAX_CARBS_STORABLE + 1)


'''Returns the number of carbs burned per mile'''
def carbs_per_mile(miles: int) -> int: 
    return 50 * miles + 5 


'''Returns the number of bananas to purchase to meet carbs demand'''
def bananas_for_carbs(carbs: int) -> int:
    return math.ceil(carbs / 27)


def carbs_for_bananas(bananas: int) -> int:
    return 27 * bananas


def f(t: int, miles: [int], prices: [int], cache) -> int:
    if t in cache:
        return cache[t]

    # Base case
    if t == 0:
        for bananas in range(19):
            banana_carbs = carbs_for_bananas(bananas)
            necessary_carbs = carbs_per_mile(miles[t])
            if banana_carbs < necessary_carbs:
                continue
            excess_carbs = banana_carbs - necessary_carbs

            today_cost = bananas * prices[t]
            for storage_amount in range(min(excess_carbs, MAX_CARBS_STORABLE) + 1):
                cache[t][storage_amount] = min(today_cost, cache[t][storage_amount])
        return cache[t]

    # Recursive case: try every banana/previously-stored-carbs amounts
    excess_carbs = 0
    for bananas, previous_storage in itertools.product(BANANAS_RANGE, STORAGE_RANGE):
        previous_excess_carbs = excess_carbs
        banana_carbs = carbs_for_bananas(bananas)
        necessary_carbs = max(carbs_per_mile(miles[t]) - previous_storage, 0)
        if banana_carbs < necessary_carbs:
            continue
        excess_carbs = banana_carbs - necessary_carbs

        today_cost = bananas * prices[t]
        for storage_amount in range(previous_excess_carbs, min(excess_carbs, MAX_CARBS_STORABLE) + 1):
            cache[t][storage_amount] = min(
                    today_cost + f(t - 1, miles, prices, cache)[previous_storage], 
                    cache[t][storage_amount]) 
    return cache[t]


def main():
    N = 2
    cache = collections.defaultdict(lambda: collections.defaultdict(lambda: sys.maxsize))
    miles = [3, 4]
    prices = [13, 16]

    final_state = f(0, miles, prices, cache)
    actual_cost = min(final_state.values())

    print("Expected: ", math.ceil((50 * 3 + 5) / 27) * prices[0])
    print("Actual: ", actual_cost)

    final_state = f(1, miles, prices, cache)
    actual_cost = min(final_state.values())

    print("Expected: ", math.ceil((50 * (3 + 4) + 5) / 27) * prices[0])
    print("Actual: ", actual_cost)

    # Generate test cases
    for t in range(5):
        start = time.time()
        cache = collections.defaultdict(lambda: collections.defaultdict(lambda: sys.maxsize))
        N = np.random.randint(1, 6e2)
        miles = np.random.randint(1, 10, N).tolist()
        prices = np.random.randint(1, 10, N).tolist()
        min_cost = min(f(N - 1, miles, prices, cache).values())
        end = time.time()

        with open('input' + str(t), 'w') as fh:
            fh.write(str(N))
            for i in range(N):
                fh.write("{} {}".format(miles[i], prices[i]))

        with open('output' + str(t), 'w') as fh:
            fh.write(str(min_cost))

        print("{} items: {}s with min cost {}".format(N, end - start, min_cost))


if __name__ == '__main__':
    main()
