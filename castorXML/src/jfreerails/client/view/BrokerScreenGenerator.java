/*
 * BrokerScreenGenerator.java
 *
 * Created on January 26, 2005, 1:31 PM
 */

package jfreerails.client.view;

import static jfreerails.world.accounts.Transaction.Category.BOND;

import java.text.DecimalFormat;

import jfreerails.controller.FinancialDataGatherer;
import jfreerails.controller.StockPriceCalculator;
import jfreerails.controller.StockPriceCalculator.StockPrice;
import jfreerails.world.common.GameCalendar;
import jfreerails.world.common.GameTime;
import jfreerails.world.common.Money;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.top.ITEM;
import jfreerails.world.top.ItemsTransactionAggregator;
import jfreerails.world.top.ReadOnlyWorld;

/**
 * 
 * @author smackay
 * @author Luke
 */

public class BrokerScreenGenerator {

    private static final DecimalFormat DC = new DecimalFormat("#,###");

    private FinancialDataGatherer dataGatherer;

    private GameCalendar cal;

    public String playername;

    public String year;

    public Money cash;

    public Money loansTotal;

    public Money netWorth;

    public Money pricePerShare;

    public String publicShares;

    public String treasuryStock;

    public String othersRRsStockRows;

    /** Creates a new instance of BrokerScreenGenerator */
    public BrokerScreenGenerator(ReadOnlyWorld w, FreerailsPrincipal principal) {
        dataGatherer = new FinancialDataGatherer(w, principal);

        int playerId = w.getID(principal);
        this.playername = w.getPlayer(playerId).getName();

        this.cal = (GameCalendar) w.get(ITEM.CALENDAR);
        GameTime time = w.currentTime();
        final int startyear = cal.getYear(time.getTicks());
        this.year = String.valueOf(startyear);
        this.cash = w.getCurrentBalance(principal);

        ItemsTransactionAggregator aggregator = new ItemsTransactionAggregator(
                w, principal);

        aggregator.setCategory(BOND);
        this.loansTotal = aggregator.calculateValue();

        this.publicShares = DC.format(dataGatherer.sharesHeldByPublic());
        this.netWorth = dataGatherer.netWorth();
        StockPrice[] stockPrices = (new StockPriceCalculator(w)).calculate();
        this.pricePerShare = stockPrices[playerId].currentPrice;
        this.treasuryStock = DC.format(dataGatherer.treasuryStock());

        StringBuffer otherRRsStakes = new StringBuffer();
        int[] stockInThisRRs = dataGatherer.getStockInThisRRs();

        for (int i = 0; i < stockInThisRRs.length; i++) {
            if (i != playerId && stockInThisRRs[i] > 0) {
                String otherRRName = w.getPlayer(i).getName();
                String otherRRStake = DC.format(stockInThisRRs[i]);
                otherRRsStakes.append("<tr> ");
                otherRRsStakes.append("<td> </td>");
                otherRRsStakes.append("<td> </td>");
                otherRRsStakes.append("<td>" + otherRRName + "</td>");
                otherRRsStakes.append("<td>" + otherRRStake + "</td>");
                otherRRsStakes.append("</tr>");
            }
        }
        othersRRsStockRows = otherRRsStakes.toString();
    }
}
