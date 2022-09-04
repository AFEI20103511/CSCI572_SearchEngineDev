import json
import csv


# to finish, strip of leading http(s) and tail "/"
class urlModify:
    @staticmethod
    def urlReformat(urls):
        index = 0
        for url in urls:
            url = url.replace('https://', '')
            url = url.replace('http://', '')
            url = url.replace('www.', '')
            if url[-1] == '/':
                url = url[:-1]
            urls[index] = url
            index += 1


if __name__ == '__main__':
    file = open('Correlation_ver2.csv', 'w', newline='')
    header = ['Queries', 'Number of Overlapping Results', 'Percent Overlap', 'Spearman Coefficient']
    writer = csv.DictWriter(file, fieldnames=header)
    writer.writerow({'Queries': 'Queries',
                     'Number of Overlapping Results': ' Number of Overlapping Results',
                     'Percent Overlap': ' Percent Overlap',
                     'Spearman Coefficient': ' Spearman Coefficient'})


    with open('Google_Result1.json') as reference:
        data1 = json.load(reference)
        with open('hw1.json') as output:
            data2 = json.load(output)
            queryNo = 1
            # get global values
            gloOverlapCount = 0.0
            gloOverlapPerct = 0.0
            glbRho = 0.0
            # get key list of google reference, 100 in total
            for key in data1:
                # get arr of key in google reference
                googleArr = data1.get(key)
                urlModify.urlReformat(googleArr)

                # get arr of key in output
                outputArr = data2.get(key)
                urlModify.urlReformat(outputArr)

                dSqr = 0
                rho = 0.0
                overlapCount = 0
                googleRank = 1
                sameRank = False
                for url in googleArr:
                    bingRank = 1
                    for bingUrl in outputArr:
                        if url == bingUrl:
                            dSqr += (googleRank - bingRank) ** 2
                            overlapCount += 1
                            if googleRank == bingRank:
                                sameRank = True
                        else:
                            bingRank += 1
                    googleRank += 1

                gloOverlapCount = (gloOverlapCount * queryNo + overlapCount)/(queryNo + 1)

                if overlapCount > 0:
                    if overlapCount > 1:
                        rho = 1 - (6 * dSqr) / (overlapCount * (overlapCount ** 2 - 1))

                    else:
                        if sameRank:
                            rho = 1

                overLapPercent = overlapCount * 10.0
                glbRho = (queryNo * glbRho + rho) / (queryNo + 1)
                gloOverlapPerct = (gloOverlapPerct * queryNo + overLapPercent) / (queryNo + 1)
                writer.writerow({'Queries': 'Query ' + str(queryNo),
                                 'Number of Overlapping Results': " " + str(overlapCount),
                                 'Percent Overlap': " " + str(overLapPercent),
                                 'Spearman Coefficient': " " + str(rho)})
                queryNo += 1

            writer.writerow({'Queries': "Averages",
                             'Number of Overlapping Results': " " + str(gloOverlapCount),
                             'Percent Overlap': " " + str(gloOverlapPerct),
                             'Spearman Coefficient': " " + str(glbRho)})