% Generated by roxygen2: do not edit by hand
% Please edit documentation in R/zemissions_xml_emission_controls.R
\name{module_emissions_emission_controls_xml}
\alias{module_emissions_emission_controls_xml}
\title{module_emissions_emission_controls_xml}
\usage{
module_emissions_emission_controls_xml(command, ...)
}
\arguments{
\item{command}{API command to execute}

\item{...}{other optional parameters, depending on command}
}
\value{
Depends on \code{command}: either a vector of required inputs,
a vector of output names, or (if \code{command} is "MAKE") all
the generated outputs: \code{EF_controls.xml}, \code{EF_controls_user.xml}
\code{EF_controls_USA.xml}, \code{EF_controls_user_USA.xml}
}
\description{
Construct XML data structure for \code{emission_factor_controls.xml}.
}
