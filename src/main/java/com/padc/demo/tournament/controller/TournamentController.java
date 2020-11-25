package com.padc.demo.tournament.controller;

import com.padc.demo.core.IService;
import com.padc.demo.tournament.domain.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Controller
public class TournamentController
{
    private final IService<Tournament> iTournamentService;

    // https://stackoverflow.com/questions/40620000/spring-autowire-on-properties-vs-constructor
    @Autowired
    public TournamentController(IService<Tournament> iTournamentService)
    {
        this.iTournamentService = iTournamentService;
    }

    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN"})
    @GetMapping("/turnering/turnering_side/{id}")
    public String tournamentPage(@PathVariable("id") long id, Model model)
    {
        try
        {
            Tournament tournament = iTournamentService.findById(id);
            model.addAttribute("tournament", tournament);
            return ("/turnering/turnering_side");
        }
        catch (EntityNotFoundException entityNotFoundException)
        {
            entityNotFoundException.printStackTrace(); // Goes to System.err
            entityNotFoundException.printStackTrace(System.out);
            return "/velkommen";
        }

        /**
         * Håndtere exceptions
         * https://stackoverflow.com/questions/54395695/what-are-the-best-practices-to-handler-or-throw-exceptions-in-a-spring-boot-appl
         * https://stackoverflow.com/questions/55283605/spring-mvc-should-service-return-optional-or-throw-an-exception
         * https://keepgrowing.in/category/java/page/2/
         * https://stackoverflow.com/questions/49316751/spring-data-jpa-findone-change-to-optional-how-to-use-this
         * https://stackoverflow.com/questions/60608873/optional-class-in-spring-boot
         * https://stackoverflow.com/questions/42993428/throw-exception-in-optional-in-java8/42993594
         * https://stackabuse.com/guide-to-optional-in-java-8/
         * https://medium.com/faun/working-on-null-elegantly-with-java-optional-62f5e65869c5
         */
    }

    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN"})
    @GetMapping("/turnering/opret_turnering")
    public String showCreateTournament(Tournament tournament, Model model)
    {
        model.addAttribute("tournament", tournament);

        return "/turnering/opret_turnering";
    }

    /**
     * Checks if the input id valid or not.
     * If it is valid, then the data gets saved in the database, and should redirect back to
     * administration site (does not exist yet).
     * If it is not valid, then print out the error in the standard error stream,
     * sends the error back to the HTML (what is written in the annotation over the fields in Tournament),
     * sends the data that was in the form back to the HTML,
     * and redirect to the showCreateTournament method
     * @param tournament
     * @param bindingResult
     * @param model
     * @return String
     */
    @Secured({"ROLE_ORGANIZER", "ROLE_ADMIN"})
    @PostMapping("/turnering/opret_turnering")
    public String createTournament(@Valid Tournament tournament, BindingResult bindingResult, Model model)
    {
        if(!bindingResult.hasErrors())
        {
            iTournamentService.save(tournament);
        }
        else
        {
            System.err.println(bindingResult);
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("tournament", tournament);

            return "/turnering/opret_turnering";
        }
        return "redirect:/velkommen";
    }
}
