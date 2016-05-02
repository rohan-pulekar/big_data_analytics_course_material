# import the inaugural corpus since we will do analysis on that corpus
from nltk.corpus import inaugural

# import the wordnet corpus since we will use that to find synonyms and hyponyms
from nltk.corpus import wordnet

# create Text object out of inaugural corpus words
all_inaugural_words_text = nltk.Text(inaugural.words())

# create a frequency distribution of inaugural corpus words that are longer than 7 chars
fdist = FreqDist([word.lower() for word in all_inaugural_words_text if len(word) > 7])

# loop through the 10 most common words
for fdist_of_ten_common_words in fdist.most_common(10):

	# get the word 
	word = fdist_of_ten_common_words[0]

	# create a hynonyms list
	hyponyms_list = list()

	# loop through the synonyms sets of this word
	for synset in wordnet.synsets(word):

		# loop through the hyponyms of the synonym set
		for hyponym_synset in synset.hyponyms():

			# add lemma names of this hyponym set to the list of hyponyms
			hyponyms_list = hyponyms_list + hyponym_synset.lemma_names()	

	# convert the strings in hyponyms list from unicode to utf-8 and to lower case
	hyponyms_list = [str(hyponym).lower() for hyponym in hyponyms_list]

	# get the unique synonyms and sort them
	hyponyms_set = sorted(set(hyponyms_list))	

	# print hyponyms for the word
	print("\nHyponyms of '{}': {}".format(word, hyponyms_set))
	print("Number of hyponyms for '{}': {}".format(word, len(hyponyms_set)))