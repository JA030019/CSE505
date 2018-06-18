
# the input form the function can take is list, whose elements are integers or list of integers
# the output will be a flatten list without hierarchy, whose elements are integers in the input list


def mystery(x):
    if type(x) is int:
        yield x
    else:
        for e in x:
            for y in mystery(e):
                yield y


def mystery2(x, thk):
    if type(x) is int:
        thk(x)
    else:
        l = len(x)
        for i in range(0, l):
            mystery2(x[i], thk)
    return result


def thk(x):
    global result
    result.append(x)


result = []
input = [[1, 2, 4, [24], 3], [[1, 3, 4], [2], [23]]]
output = [x for x in mystery(input)]
output_2 = mystery2(input, thk)
print(output)
print(output_2)