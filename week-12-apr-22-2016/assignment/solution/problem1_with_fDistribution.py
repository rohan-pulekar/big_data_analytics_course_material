# the below import is to make sure decimal numbers are shown after division
from __future__ import division

# import gutenberg corpus
from nltk.corpus import gutenberg

print("\nRelative frequencies AS FRACTIONS (not percentage) of modals in gutenberg text")
print("\ncan\tcould\tmay\tmight\twill\twould\tshould")

# loop for each text of gutenberg
for fileid in gutenberg.fileids():

	# get frequency distribution in that file
	fdist = nltk.FreqDist(gutenberg.words(fileid))

	# get relative frequency of each modal 
	relative_frequency_of_can = round((fdist["can"])/(len(gutenberg.words(fileid))), 4)
	relative_frequency_of_could = round((fdist["could"])/(len(gutenberg.words(fileid))), 4)
	relative_frequency_of_may = round((fdist["may"])/(len(gutenberg.words(fileid))), 4)
	relative_frequency_of_might = round((fdist["might"])/(len(gutenberg.words(fileid))), 4)
	relative_frequency_of_will = round((fdist["will"])/(len(gutenberg.words(fileid))), 4)
	relative_frequency_of_would = round((fdist["would"])/(len(gutenberg.words(fileid))), 4)
	relative_frequency_of_should = round((fdist["should"])/(len(gutenberg.words(fileid))), 4)

	# print the relative frequencies
	print("{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}".format(relative_frequency_of_can,relative_frequency_of_could,relative_frequency_of_may,relative_frequency_of_might,relative_frequency_of_will,relative_frequency_of_would,relative_frequency_of_should,fileid))