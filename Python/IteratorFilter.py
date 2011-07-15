class Pet(object):

    @staticmethod
    def from_list(args_list):
        kwargs = dict()
        type_str = args_list[0]
        if type_str == "Dog":
            pet_clss = Dog
        elif type_str == "Cat":
            pet_clss = Cat
        else:
            raise ValueError("Unknown Pet type '%s'" % type_str)
        return pet_clss(**dict([[args_list[x], args_list[x+1]] for x in range(1, len(args_list)-1, 2)]))

    def __init__(self, **kwargs):
        self.name = kwargs['name']
        self.breed = kwargs['breed']
        self.owner = kwargs.get('owner', None)
        self.color = kwargs['color']

    def was_sold(self):
        if self.owner is not None:
            return True
        else:
            return False


class Cat(Pet):
    
    def __init__(self, **kwargs):
        Pet.__init__(self, **kwargs)
        self.meows = kwargs.get("meows", True)


class Dog(Pet):
    
    def __init__(self, **kwargs):
        Pet.__init__(self, **kwargs)
        self.barks = kwargs.get("barks", True)


class FilterablePetStore(object):

    def __iter__(self):
        raise NotImplementedError()

    def filter(self, filter_func):
        return PetStoreView(self, filter_func)

    def was_sold(self):
        return self.filter(lambda pet: pet.was_sold())

    def is_type(self, test_type):
        return self.filter(lambda pet: isinstance(pet, test_type))

    def is_color(self, color):
        return self.filter(lambda pet: pet.color.upper() == color.upper())

    def is_tabby(self):
        return self.filter(lambda pet: pet.color.upper().endswith("TABBY"))

    def as_list(self):
        return [pet for pet in self]


class PetStore(FilterablePetStore):
    
    def __init__(self, list_of_pets):
        self.pets = list_of_pets

    def __getitem__(self, index):
        assert isinstance(index, int), "Index must be an integer"
        return self.pets[index]

    def __iter__(self):
        return PetStoreView(self, lambda pet: True)


class PetStoreView(FilterablePetStore):

    def __init__(self, pets, filter_func):
        self._pets = pets
        self._location = 0
        self._filter_function = filter_func

    def __iter__(self):
        return self

    def next(self):
        try:
            try:
                next_item = self._pets.next()
            except AttributeError:
                next_item = self._pets[self._location]
                self._location += 1
        except IndexError:
            raise StopIteration()
        if self._filter_function(next_item):
            return next_item
        else:
            return self.next()


class PetStore2(FilterablePetStore):

    def __init__(self, list_of_pets):
        self.pets = list_of_pets

    def __iter__(self):
        return iter(self.pets)

    def filter(self, filter_func):
        return PetStore2(filter(filter_func, self.pets))

    def as_list(self):
        return self.pets


class PetStore3(FilterablePetStore):

    def __init__(self, list_of_pets):
        self.pets = list_of_pets

    def all(self):
	    self._filter = self.pets
	    return self	

    def __iter__(self):
        for x in self._filter:
            yield x

    def filter(self, filter_func):
        import itertools		
        self._filter = itertools.ifilter(filter_func, self._filter)
        return self


if __name__ == "__main__":
    import cProfile
    import csv
    
    list_of_pets = []
    with open("pets.csv", "rb") as csv_file:
        reader = csv.reader(csv_file, delimiter="\t", quotechar="|")
        for row in reader:
            list_of_pets.append(Pet.from_list(row))
        del reader

    print "### Profiling iterators for filtering ###"
    print ""
    pets1 = PetStore(list_of_pets)
    cProfile.run('for pet in pets1.is_type(Cat).is_tabby().was_sold(): print pet.name')
    
    print "### Profiling filter() for filtering ###"
    print ""
    pets2 = PetStore2(list_of_pets)
    cProfile.run('for pet in pets2.is_type(Cat).is_tabby().was_sold(): print pet.name')

    print "### Profiling itertools for filtering ###"
    print ""
    pets3 = PetStore3(list_of_pets)
    cProfile.run('for pet in pets3.all().is_type(Cat).is_tabby().was_sold(): print pet.name')

