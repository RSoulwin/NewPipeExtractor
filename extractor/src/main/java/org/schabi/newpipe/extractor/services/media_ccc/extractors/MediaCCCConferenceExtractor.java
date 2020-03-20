package org.schabi.newpipe.extractor.services.media_ccc.extractors;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.channel.ChannelTabExtractor;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class MediaCCCConferenceExtractor extends ChannelExtractor {
    public static final String EVENTS_TAB = "events";

    private JsonObject conferenceData;

    public MediaCCCConferenceExtractor(StreamingService service, ListLinkHandler linkHandler) {
        super(service, linkHandler);
    }

    @Override
    public String getAvatarUrl() throws ParsingException {
        return conferenceData.getString("logo_url");
    }

    @Override
    public String getBannerUrl() throws ParsingException {
        return conferenceData.getString("logo_url");
    }

    @Override
    public String getFeedUrl() throws ParsingException {
        return null;
    }

    @Override
    public long getSubscriberCount() throws ParsingException {
        return -1;
    }

    @Override
    public String getDescription() throws ParsingException {
        return null;
    }

    @Override
    public List<ChannelTabExtractor> getTabs() throws ParsingException {
        List<ChannelTabExtractor> tabs = new ArrayList<>();

        tabs.add(new MediaCCCConferenceEventsExtractor(getService(), (ListLinkHandler) getLinkHandler(), conferenceData));

        return tabs;
    }

    @Override
    public void onFetchPage(@Nonnull Downloader downloader) throws IOException, ExtractionException {
        try {
            conferenceData = JsonParser.object().from(downloader.get(getUrl()).responseBody());
        } catch (JsonParserException jpe) {
            throw new ExtractionException("Could not parse json returnd by url: " + getUrl());
        }
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return conferenceData.getString("title");
    }

    @Nonnull
    @Override
    public String getOriginalUrl() throws ParsingException {
        return "https://media.ccc.de/c/" + conferenceData.getString("acronym");
    }
}
