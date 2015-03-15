# Get photos for a location #
http://www.flickr.com/services/api/flickr.photos.search.html

## Example ##
http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=4a0e553ddd58e3428eaef8f7a7edc899&min_taken_date=20100101&has_geo=1&lat=41.383263&lon=-8.780026&radius=10&extras=geo%2C+url_m%2C+url_sq&format=rest&api_sig=048215caf95438f902ee8915002b809c

This gives us almost everything we need. Check the **extras** options.

## Please note that they limit the output: ##

> Geo queries require some sort of limiting agent in order to prevent the database from crying. This is basically like the check against "parameterless searches" for queries without a geo component.
> A tag, for instance, is considered a limiting agent as are user defined min\_date\_taken and min\_date\_upload parameters — If no limiting factor is passed we return only photos added in the last 12 hours (though we may extend the limit in the future).

## GPS coordinates in the answer to the search ##
We can pass _extra_ parameters when using flickr.photos.search:

**extras (Optional)**: A comma-delimited list of extra information to fetch for each returned record. Currently supported fields are: description, license, date\_upload, date\_taken, owner\_name, icon\_server, original\_format, last\_update, geo, tags, machine\_tags, o\_dims, views, media, path\_alias, url\_sq, url\_t, url\_s, url\_m, url\_z, url\_l, url\_o

# EXIF #
Using flickr.photos.getExif
http://www.flickr.com/services/api/flickr.photos.getExif.html