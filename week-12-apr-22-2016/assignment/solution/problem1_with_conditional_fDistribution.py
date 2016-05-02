# the below import is to make sure decimal numbers are shown after division
from __future__ import division

# import gutenberg corpus
from nltk.corpus import gutenberg

print("\nRelative frequencies AS FRACTIONS (not percentage) of modals in gutenberg text\n")

conditional_fdist = nltk.ConditionalFreqDist(
	(fileid, word) 
	for fileid in gutenberg.fileids()
	for word in gutenberg.words(fileid))

modals = ["can", "could", "may", "might", "will", "would", "should"]

#conditional_fdist.tabulate(samples=modals)

print("%-30s" % "Category", end="")
for modal in modals: # column headings
	print("%10s" % modal, end="")
print()
for category in gutenberg.fileids():
	print("%-30s" % category, end="") # row heading
	for modal in modals: # for each word
		relative_freq_of_modal = round(conditional_fdist[category][modal]/conditional_fdist[category].N(), 4)
		print("%10s" % format(float(relative_freq_of_modal), '.4f'), end="") # print table cell
	print()