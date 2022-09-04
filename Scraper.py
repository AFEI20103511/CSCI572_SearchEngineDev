import json
import string

from bs4 import BeautifulSoup
from time import sleep
import requests
from random import randint
from html.parser import HTMLParser
import time

USER_AGENT = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) '
                            'Chrome/70.0.3538.77 Safari/537.36'}


class SearchEngine:
    @staticmethod
    def search(query, sleep=True):
        # if sleep:  # Prevents loading too many pages too soon
        #     time.sleep(randint(10, 50))
        temp_url = '+'.join(query.split())  # for adding + between words for the query
        url = 'http://www.bing.com/search?q=' + temp_url.rstrip('\n') + '&count=30'
        soup = BeautifulSoup(requests.get(url, headers=USER_AGENT).text, "html.parser")
        new_results = SearchEngine.scrape_search_result(soup)
        return new_results

    @staticmethod
    def scrape_search_result(soup):
        raw_results = soup.find_all("li", attrs={"class": "b_algo"})
        results = []
        duplicateCheck = set()
        count = 0
        # implement a check to get only 10 results and also check that URLs must not be duplicated
        if len(raw_results) >= 10:
            for result in raw_results:
                if count < 10:
                    if result not in duplicateCheck:
                        link = result.find('a').get('href')
                        header = link[0:4]
                        if link is not None and header == 'http':
                            results.append(link)
                            duplicateCheck.add(link)
                            count += 1
                else:
                    break
        else:
            for result in raw_results:
                link = result.find('a').get('href')
                results.append(link)

        return results


#############Driver code############
if __name__ == '__main__':
    reDic = {}
    file = open('supplement.txt', 'r')
    lines = file.readlines()

    for line in lines:
        query = line.rstrip()
        re = SearchEngine.search(query)
        reDic[query] = re
        # print(line)

    y = json.dumps(reDic, indent=4, separators=(',', ':'))
    #
    print(y)
    #
    jsonFile = open("HW1_supplement.json", "w")
    jsonFile.write(y)
    jsonFile.close()

####################################
